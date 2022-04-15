package com.javaproject.seckill.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaproject.seckill.mapper.GoodsMapper;
import com.javaproject.seckill.pojo.Goods;
import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dumplings
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> getGoodsVo() {
        return goodsMapper.getGoodsVo();
    }


    @Override
    public GoodsVo getGoodsVoDetailByGoodsId(Long goodsId) {

        return goodsMapper.getGoodsVoDetailByGoodsId(goodsId);
    }
}
