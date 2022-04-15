package com.javaproject.seckill.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaproject.seckill.pojo.SeckillGoods;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dumplings
 */
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

   int updateStockCount(Long seckillGoodId);
}
