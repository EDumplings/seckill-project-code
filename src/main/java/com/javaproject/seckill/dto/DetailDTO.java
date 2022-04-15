package com.javaproject.seckill.dto;

import com.javaproject.seckill.pojo.User;
import com.javaproject.seckill.vo.GoodsVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailDTO {

    private User user;
    private GoodsVo goodsVo;
    private int seckillStatus;
    private int remainSeconds;

}
