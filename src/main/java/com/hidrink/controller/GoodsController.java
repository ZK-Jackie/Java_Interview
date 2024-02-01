package com.hidrink.controller;

import com.hidrink.annotaion.Recorder;
import com.hidrink.pojo.*;
import com.hidrink.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//商品
@Slf4j
@RestController

public class GoodsController {
    @Autowired
    public GoodsService goodsService;

    //商品页数
    @GetMapping("/getGoods")
    public Result getGoods(@RequestParam(name = "pageNow", defaultValue = "1") Integer pageNow,
                          @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize,
                          @RequestParam(name = "priceOrder", defaultValue = "1") Integer priceOrder,
                           Integer minPrice, Integer maxPrice, String name){
        GoodsPageInfo goodsPageInfo = new GoodsPageInfo(name,pageNow,pageSize,priceOrder,minPrice,maxPrice,null);
        log.info("前端请求所有商品信息，第{}页，共{}条，条件为：{},{},{},{}",
                goodsPageInfo.getPageNow(), goodsPageInfo.getPageSize(), goodsPageInfo.getName(),
                goodsPageInfo.getPriceOrder(), goodsPageInfo.getMinPrice(), goodsPageInfo.getMaxPrice());
        return Result.success(goodsService.getGoodsPageInfo(goodsPageInfo));
    }

}
