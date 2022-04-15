package com.javaproject.seckill.controller;


import com.javaproject.seckill.config.AccessLimit;
import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.rabbitmq.MQSender;
import com.javaproject.seckill.rabbitmq.SeckillMessage;
import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.service.IOrderService;
import com.javaproject.seckill.service.ISeckillGoodsService;
import com.javaproject.seckill.utils.JsonUtil;
import com.javaproject.seckill.vo.GoodsVo;
import com.javaproject.seckill.vo.RespBean;
import com.javaproject.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**Tps 30
 * TPs 364
 */

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private MQSender mqSender;
    @Autowired
    private IOrderService orderService;

    /**
    @RequestMapping("/doSeckill")
    public String doSeckill(User user, Model model, @RequestParam("goodsId") Long goodsId ){

        if(user == null){
            return "login";
        }

        model.addAttribute("user", user);

        // 判断是否重复抢购
        Boolean dumpSeckillOrder = seckillGoodsService.isDumpSeckillOrder(user.getId(), goodsId);
        if(dumpSeckillOrder){
            model.addAttribute("errmsg", RespBeanEnum.DUMP_SECKILL_ORDER.getMessage());
            return "seckillFail";
        }

        // 预减库存
        Long stock = seckillGoodsService.preDecStock(goodsId);
        if(stock < 0){
            return "seckillFail";
        }



        /**
        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoDetailByGoodsId(goodsId);

        if(goods.getStockCount() <= 0){
            model.addAttribute("errmsg", RespBeanEnum.EMPYT_STOCK.getMessage());
            return "seckillFail";
        }
         Order order = seckillGoodsService.doSeckill(user, goods);

         model.addAttribute("goods", goods);
         model.addAttribute("order", order);
        */


        /**
        // 请求入队， 立即返回排队
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        model.addAttribute("watting", RespBeanEnum.Order_Watting.getMessage());
        return "seckillWating";
    }
    */

    @RequestMapping(value = "/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean<Integer> doSeckill(User user, @RequestParam("goodsId") Long goodsId , @PathVariable String path){

        if(user == null){
            return RespBean.error(RespBeanEnum.SESSEION_ERROR);
        }
        // 判断秒杀路径是否存在
        Boolean check = seckillGoodsService.checkPath(user, goodsId, path);
        if(!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEAGAL);
        }
        // 判断是否重复抢购
        Boolean dumpSeckillOrder = seckillGoodsService.isDumpSeckillOrder(user.getId(), goodsId);
        if(dumpSeckillOrder){
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }

        // 预减库存
        Long stock = seckillGoodsService.preDecStock(goodsId);
        if(stock < 0){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        /**
         // 判断库存
         GoodsVo goods = goodsService.getGoodsVoDetailByGoodsId(goodsId);

         if(goods.getStockCount() <= 0){
         model.addAttribute("errmsg", RespBeanEnum.EMPYT_STOCK.getMessage());
         return "seckillFail";
         }
         Order order = seckillGoodsService.doSeckill(user, goods);

         model.addAttribute("goods", goods);
         model.addAttribute("order", order);
         */

        // 请求入队， 立即返回排队
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));

        return RespBean.success(0);
    }

    @RequestMapping(value = "/result" , method = RequestMethod.GET)
    @ResponseBody
    public RespBean<Long> getResult(User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSEION_ERROR);
        }

        Long orderId = seckillGoodsService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    @AccessLimit(second = 60, maxCount = 60, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean<String> getPath(User user, Long goodsId){
        if(user == null){
            return RespBean.error(RespBeanEnum.SESSEION_ERROR);
        }
        String str = orderService.createPath(user, goodsId);

        return RespBean.success(str);

    }


}
