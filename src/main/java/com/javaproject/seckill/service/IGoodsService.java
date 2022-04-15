package com.javaproject.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.javaproject.seckill.pojo.Goods;
import com.javaproject.seckill.vo.GoodsVo;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dumplings
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> getGoodsVo();

    GoodsVo getGoodsVoDetailByGoodsId(Long goodsId);
}
