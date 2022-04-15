package com.javaproject.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IUserService;
import com.javaproject.seckill.utils.CookieUtil;
import com.javaproject.seckill.vo.RespBean;
import com.javaproject.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class AccessInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){

            User user = getUser(request, response);
            UserContext.setUser(user);

            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit == null){
                return true;
            }
            int second = accessLimit.second();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if(needLogin){

                if(user == null){
                    render(response, RespBeanEnum.SESSEION_ERROR);
                    return  false;
                }

                key += ":" + user.getId();
            }

            ValueOperations valueOperations = redisTemplate.opsForValue();
            Integer count = (Integer) valueOperations.get(key);
            if(count == null){
                valueOperations.set(key ,1, second, TimeUnit.SECONDS);
            }
            else if(count < maxCount){
                valueOperations.increment(key);
            }
            else{
                render(response, RespBeanEnum.ACCESS_LIMIT_REACHED);
                return false;
            }
        }

        return true;
    }

    private void render(HttpServletResponse response, RespBeanEnum sesseionError) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        RespBean error = RespBean.error(sesseionError);
        writer.write(new ObjectMapper().writeValueAsString(error));
        writer.flush();
        writer.close();
    }

    public User getUser(HttpServletRequest request, HttpServletResponse response){

        String ticket = CookieUtil.getCookieValue(request, "userTicket");

        if(!StringUtils.hasLength(ticket))
            return null;

        return iUserService.getByUserTicket(ticket, request, response);

    }

    // 防止内存泄露
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
