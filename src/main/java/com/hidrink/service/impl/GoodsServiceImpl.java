package com.hidrink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hidrink.mapper.GoodsMapper;
import com.hidrink.pojo.Goods;
import com.hidrink.pojo.GoodsPageInfo;
import com.hidrink.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    public GoodsMapper goodsMapper;

    //商品页数
    public GoodsPageInfo getGoodsPageInfo(GoodsPageInfo goodsPageInfo) {
        //1.设置分页参数
        PageHelper.startPage(goodsPageInfo.getPageNow(), goodsPageInfo.getPageSize());
        //2.将查询结果交给Page泛型
        List<Goods> data = goodsMapper.getGoods();
        //3.包装数据
        goodsPageInfo.setTableData(((Page<Goods>) data).getResult());
        //4.返回controller
        return goodsPageInfo;
    }
}