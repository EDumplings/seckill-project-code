package com.javaproject.seckill.controller;


import com.javaproject.seckill.dto.OrderDetailVo;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IOrderService;
import com.javaproject.seckill.vo.RespBean;
import com.javaproject.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dumplings
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("detail")
    @ResponseBody
    public RespBean<OrderDetailVo> detail(User user, Long orderId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSEION_ERROR);
        }

        OrderDetailVo detail = orderService.detail(orderId);

        return RespBean.success(detail);
    }

}
