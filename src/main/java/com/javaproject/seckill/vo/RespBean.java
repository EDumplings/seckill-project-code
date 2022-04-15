package com.javaproject.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean<T> {

    private int code;
    private String message;
    private T data;

    public static RespBean success(){
        return  new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    public static <T> RespBean<T> success(T data){
        return new RespBean<T>(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), data);
    }

    public static RespBean error(RespBeanEnum respBeanEnum){
        return  new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(), null);
    }

    public static <T> RespBean<T> error(RespBeanEnum respBeanEnum, T data){
        return new RespBean<T>(respBeanEnum.getCode(), respBeanEnum.getMessage(), data);
    }


}
