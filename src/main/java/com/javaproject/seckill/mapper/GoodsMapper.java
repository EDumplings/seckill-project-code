package com.javaproject.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaproject.seckill.pojo.Goods;
import com.javaproject.seckill.vo.GoodsVo;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dumplings
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> getGoodsVo();

    GoodsVo getGoodsVoDetailByGoodsId(Long goodsId);
}
