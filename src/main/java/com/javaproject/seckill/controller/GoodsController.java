package com.javaproject.seckill.controller;

import com.javaproject.seckill.dto.DetailDTO;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.service.IUserService;
import com.javaproject.seckill.vo.GoodsVo;
import com.javaproject.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, User user, Model model){
        String html = (String)redisTemplate.opsForValue().get("goodList");
        if(html != null){
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.getGoodsVo());
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", context);
        if(!StringUtils.hasLength(html)){
            redisTemplate.opsForValue().set("goodList", html, 60, TimeUnit.SECONDS);
        }
        return html;

    }
    /**
    @RequestMapping(value = "/toDetail/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String getGoodsVoDetailByGoodsId(HttpServletRequest request, HttpServletResponse response, User user, Model model, @PathVariable("goodsId") Long goodsId){

        String html = (String) redisTemplate.opsForValue().get("goodsDetail");
        if(html != null){
            return html;
        }
        model.addAttribute("user", user);

        GoodsVo goodsVo = goodsService.getGoodsVoDetailByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        int secKillStatus = 0;
        int remainSeconds = 0;

        if(nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime()-nowDate.getTime())/1000);
        }
        else if(nowDate.after(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        }
        else{
            secKillStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(StringUtils.hasLength(html)){
            redisTemplate.opsForValue().set("goodsDetail", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }
    */
    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean<DetailDTO> getGoodsVoDetailByGoodsId(User user, @PathVariable("goodsId") Long goodsId){

        GoodsVo goodsVo = goodsService.getGoodsVoDetailByGoodsId(goodsId);

        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        int secKillStatus = 0;
        int remainSeconds = 0;

        if(nowDate.before(startDate)){
            remainSeconds = (int)((startDate.getTime()-nowDate.getTime())/1000);
        }
        else if(nowDate.after(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        }
        else{
            secKillStatus = 1;
            remainSeconds = 0;
        }

        DetailDTO detailDTO = new DetailDTO();
        detailDTO.setGoodsVo(goodsVo);
        detailDTO.setUser(user);
        detailDTO.setRemainSeconds(remainSeconds);
        detailDTO.setSeckillStatus(secKillStatus);

       return RespBean.success(detailDTO);
    }


}
