package com.javaproject.seckill.controller;


import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.rabbitmq.MQSender;
import com.javaproject.seckill.vo.RespBean;
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
@RequestMapping("/user")
public class UserController {

    @RequestMapping("info")
    public RespBean<User> info(User user){


        return RespBean.success(user);
    }




}
