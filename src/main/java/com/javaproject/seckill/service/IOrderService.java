package com.javaproject.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaproject.seckill.dto.OrderDetailVo;
import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.pojo.User;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dumplings
 */
public interface IOrderService extends IService<Order> {

    OrderDetailVo detail(Long orderId);

    String createPath(User user, Long goodsId);
}
