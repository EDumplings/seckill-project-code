package com.javaproject.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendSeckillMessage(String msg){
        log.info("MQSender发送消息到queue" + msg);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.msg" , msg);
    }





}
