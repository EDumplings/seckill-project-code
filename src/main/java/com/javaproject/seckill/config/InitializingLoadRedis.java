package com.javaproject.seckill.config;

import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sun.reflect.generics.tree.ClassSignature;

import java.util.List;

/**
 * 加载时初始化
 */
@Component
@Slf4j
public class InitializingLoadRedis implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemoryTag emptyStockMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("InitializingLoadRedis 加载消息");
        List<GoodsVo> list = goodsService.getGoodsVo();

        if(CollectionUtils.isEmpty(list)){
            return ;
        }

        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
