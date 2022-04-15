package com.javaproject.seckill.rabbitmq;

import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.service.IOrderService;
import com.javaproject.seckill.service.ISeckillGoodsService;
import com.javaproject.seckill.service.ISeckillOrderService;
import com.javaproject.seckill.utils.JsonUtil;
import com.javaproject.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring5.processor.SpringErrorClassTagProcessor;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg){
        log.info("MQReceiver收到消息" + msg);

        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        // 处理数据库，尽量保证Redis和数据库的一致性，每次都要判断
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goods = goodsService.getGoodsVoDetailByGoodsId(goodsId);
        if(goods.getStockCount() < 1){
            return ;
        }
        String seckillOrderJson = (String) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (StringUtils.hasLength(seckillOrderJson)) {
            return;
        }
        seckillGoodsService.doSeckill(user, goodsService.getGoodsVoDetailByGoodsId(goodsId));

    }


}
