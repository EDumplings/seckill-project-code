package com.javaproject.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.ConstraintViolation;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {

    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),
    LOGIN_ERROR(10000, "登录错误"),
    MOBILE_ERROR(10001, "手机号格式不对"),
    BIND_ERROR(10002, ""),

    EMPYT_STOCK(10003, "秒杀商品不足"),
    DUMP_SECKILL_ORDER(10004, "你已订购该商品"),

    Order_Watting(10005, "正在等待"),
    SESSEION_ERROR(10006, "请登录"),
    ORDER_NOT_EXIST(10007, "订单不存在"),
    REPEATE_ERROR(10008, "订单重复"),
    EMPTY_STOCK(10009, "库存已空"),
    REQUEST_ILLEAGAL(10010, "请求错误"),
    ACCESS_LIMIT_REACHED(10011,"访问次数达到上限" );

    private final Integer code;
    private final String message;

}
