package com.javaproject.seckill.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.pojo.SeckillGoods;
import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.vo.GoodsVo;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dumplings
 */
public interface ISeckillGoodsService extends IService<SeckillGoods> {

    Boolean isDumpSeckillOrder(Long userId, Long goodsId);

    Order doSeckill(User user, GoodsVo goods);

    Long preDecStock(Long goodsId);

    Long getResult(User user, Long goodsId);

    Boolean checkPath(User user, Long goodsId, String path);
}
