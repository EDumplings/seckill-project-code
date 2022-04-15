package com.javaproject.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.vo.LoginVo;
import com.javaproject.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dumplings
 */
public interface IUserService extends IService<User> {

    RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo);

    User getByUserTicket(String userTicket, HttpServletRequest request, HttpServletResponse response);
}
