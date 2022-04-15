package com.javaproject.seckill;

import com.javaproject.seckill.service.IGoodsService;
import com.javaproject.seckill.vo.GoodsVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class SeckillProjectApplicationTests {

    @Autowired
    private IGoodsService goodsService;

    @Test
    void contextLoads() {
        long id = 3;
        GoodsVo goodsVo = goodsService.getGoodsVoDetailByGoodsId(id);

        Date date = goodsVo.getStartDate();
        System.out.println(date.toString());
        Date now = new Date();
        System.out.println((date.getTime() - now.getTime())/1000);

    }

    @Test
    public void TimeTest(){
        long nowTs = System.currentTimeMillis();
        System.out.println(nowTs);
    }

}
