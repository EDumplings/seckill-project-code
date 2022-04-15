package com.javaproject.seckill.dto;

import com.javaproject.seckill.pojo.Order;
import com.javaproject.seckill.vo.GoodsVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {

    private GoodsVo goodsVo;
    private Order order;
}
