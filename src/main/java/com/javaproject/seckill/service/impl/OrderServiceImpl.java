package com.javaproject.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.javaproject.seckill.dto.OrderDetailVo;
import com.javaproject.seckill.exception.GlobalException;
import com.javaproject.seckill.mapper.GoodsMapper;
import com.javaproject.seckill.mapper.OrderMapper;
import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IOrderService;
import com.javaproject.seckill.utils.MD5Util;
import com.javaproject.seckill.utils.UUIDUtil;
import com.javaproject.seckill.vo.GoodsVo;
import com.javaproject.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dumplings
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService{

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (null==orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsMapper.getGoodsVoDetailByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }

    @Override
    public String createPath(User user, Long goodsId) {

        String str = MD5Util.md5(UUIDUtil.uuid() + "123");

        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }
}
