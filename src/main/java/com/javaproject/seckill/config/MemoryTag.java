package com.javaproject.seckill.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MemoryTag {

    private Map<Long, Boolean> empytStockMap = new HashMap<>();

    public Boolean get(Long key){
        return empytStockMap.get(key);
    }

    public Boolean put(Long key, Boolean value) {

        return empytStockMap.put(key, value);
    }

    public Boolean remove(Long key){
        return empytStockMap.remove(key);
    }


}
