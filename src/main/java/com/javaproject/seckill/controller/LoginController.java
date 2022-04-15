package com.javaproject.seckill.controller;


import com.javaproject.seckill.service.IUserService;
import com.javaproject.seckill.vo.LoginVo;
import com.javaproject.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    public RespBean doLogin(HttpServletRequest request, HttpServletResponse response,  @Valid LoginVo loginVo){

        log.info("{}", loginVo);
        return iUserService.login(request, response, loginVo);
    }


}
