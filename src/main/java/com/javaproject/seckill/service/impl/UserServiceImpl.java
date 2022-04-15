package com.javaproject.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.javaproject.seckill.exception.GlobalException;
import com.javaproject.seckill.mapper.UserMapper;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IUserService;
import com.javaproject.seckill.utils.CookieUtil;
import com.javaproject.seckill.utils.JsonUtil;
import com.javaproject.seckill.utils.MD5Util;
import com.javaproject.seckill.utils.UUIDUtil;
import com.javaproject.seckill.vo.LoginVo;
import com.javaproject.seckill.vo.RespBean;
import com.javaproject.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dumplings
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse response, LoginVo loginVo) {


        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = userMapper.selectById(mobile);
        if(user == null) {
            throw  new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 判断密码
        if(!MD5Util.secondEncode(password, user.getSalt()).equals(user.getPassword())){
            throw  new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 同一浏览器多次登录, ticket 有值, 同一用户，不同用户
        // 不同浏览器登录，ticket无值，

        String oldTicket = (String)redisTemplate.opsForValue().get("mobile:"+ loginVo.getMobile());
        // 已经登录
        if(oldTicket != null){
            String ticket = UUIDUtil.uuid();
            redisTemplate.delete("mobile:" + loginVo.getMobile());
            redisTemplate.delete("user:" + oldTicket);
        }


        // 生成cookie uuid
        String ticket = UUIDUtil.uuid();

        redisTemplate.opsForValue().set("user:" + ticket, JsonUtil.object2JsonStr(user));
        redisTemplate.opsForValue().set("mobile:" + loginVo.getMobile(), ticket);
        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success(ticket);
    }
    


   public User getByUserTicket(String userTicket, HttpServletRequest request, HttpServletResponse response){

        if(!StringUtils.hasLength(userTicket)){
            return null;
        }

        String userJson = (String) redisTemplate.opsForValue().get("user:" + userTicket);

        User user = JsonUtil.jsonStr2Object(userJson, User.class);

        if(user != null){
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }

        return user;
   }



}
