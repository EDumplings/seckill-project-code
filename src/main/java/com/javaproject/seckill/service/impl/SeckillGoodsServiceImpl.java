package com.javaproject.seckill.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaproject.seckill.config.MemoryTag;
import com.javaproject.seckill.mapper.OrderMapper;
import com.javaproject.seckill.mapper.SeckillGoodsMapper;
import com.javaproject.seckill.mapper.SeckillOrderMapper;
import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.pojo.SeckillGoods;
import com.javaproject.seckill.pojo.SeckillOrder;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.ISeckillGoodsService;
import com.javaproject.seckill.utils.JsonUtil;
import com.javaproject.seckill.vo.GoodsVo;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.function.BiConsumer;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dumplings
 */
@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements ISeckillGoodsService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemoryTag empytStockMap;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Long> script;

    @Override
    public Boolean isDumpSeckillOrder(Long userId, Long goodsId) {

        String seckillOrder = (String)redisTemplate.opsForValue().get("order:" + userId + ":" + goodsId);

        if(StringUtils.hasLength(seckillOrder)){
            return true;
        }
        return false;

    }
    /**
    @Override
    public Long preDecStock(Long goodsId) {
        // 查看内存标记，减少Redis访问
        if(empytStockMap.get(goodsId)){
            return -1L;
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long stock = (Long) valueOperations.decrement("seckillGoods:" + goodsId);
        if(stock < 0){
            empytStockMap.put(goodsId, true);
            valueOperations.increment("seckillGoods:" + goodsId);
            return -1L;
        }
        return stock;

    }
    */
    @Override
    public Long preDecStock(Long goodsId) {
        // 查看内存标记，减少Redis访问
        if(empytStockMap.get(goodsId)){
            return -1L;
        }
        Long stock = (Long)redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if(stock < 0){
            empytStockMap.put(goodsId, true);
            return -1L;
        }
        return stock;

    }


    @Override
    @Transactional
    public Order doSeckill(User user, GoodsVo goods) {

        SeckillGoods seckillGoods = seckillGoodsMapper.selectById(goods.getId());

        int goodsResult = seckillGoodsMapper.updateStockCount(goods.getId());

        if(seckillGoods == null || goodsResult <= 0){
            redisTemplate.opsForValue().set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());

        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderMapper.insert(seckillOrder);

        redisTemplate.opsForValue().set("order:"+ user.getId() + ":" + goods.getId(), JsonUtil.object2JsonStr(seckillOrder));

        return order;
    }

    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder != null){
            return seckillOrder.getId();
        }
        else{
            if(redisTemplate.hasKey("isStockEmpty:" + goodsId)){
                return -1l;
            }
            return 0l;
        }

    }

    @Override
    public Boolean checkPath(User user, Long goodsId, String path) {
        if(goodsId == null || !StringUtils.hasLength(path))
            return false;
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
