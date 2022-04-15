package com.javaproject.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.javaproject.seckill.mapper.SeckillOrderMapper;
import com.javaproject.seckill.pojo.SeckillOrder;
import com.javaproject.seckill.service.ISeckillOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dumplings
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

}
