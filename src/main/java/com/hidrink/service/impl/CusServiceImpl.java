package com.hidrink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hidrink.mapper.CusMapper;
import com.hidrink.pojo.Cus;
import com.hidrink.pojo.CusPageInfo;

import com.hidrink.pojo.Emp;
import com.hidrink.service.CusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class CusServiceImpl implements CusService {
    @Autowired
    public CusMapper cusMapper;


    //获取顾客列表
    public CusPageInfo getCusPageInfo(CusPageInfo cusPageInfo){
        //1.设置分页参数
        PageHelper.startPage(cusPageInfo.getPageNow(), cusPageInfo.getPageSize());
        //2.将查询结果交给Page泛型
        List<Cus> data = cusMapper.getCusPage(cusPageInfo);
        //3.包装数据
        cusPageInfo.setTableData(((Page<Cus>) data).getResult());
        //4.返回controller
        return cusPageInfo;
    }

    //更新顾客信息
    @Override
    public Boolean updateCus(Cus cus) {
        cus.setUpdateTime(LocalDateTime.now());
        return cusMapper.updateCusId(cus);
    }

    @Override
    public CusPageInfo getCus(CusPageInfo cusPageInfo) {
        //1.设置分页参数
        PageHelper.startPage(cusPageInfo.getPageNow(), cusPageInfo.getPageSize());
        //2.将查询结果交给Page泛型
        List<Cus> data = cusMapper.getCus(cusPageInfo);
        //3.包装数据
        cusPageInfo.setTableData(((Page<Cus>) data).getResult());
        cusPageInfo.setTotalSize(((Page<Cus>) data).getTotal());
        //4.返回controller
        return cusPageInfo;
    }

    //添加顾客
    @Override
    public Boolean addCus(Cus cus) {
        cus.setCreateTime(LocalDateTime.now());
        cus.setUpdateTime(LocalDateTime.now());
        cusMapper.insertCus(cus);
        return true;
    }

    @Override
    public HashMap<String, Integer> getDaily() {
        HashMap<String,Integer> result = new HashMap<>();
        result.put("totalCustomer",cusMapper.countCus());
        result.put("totalMaleCustomer",cusMapper.countMale());
        result.put("totalFemaleCustomer",cusMapper.countFemale());
        result.put("todayRegister",cusMapper.countTodayRegister());
        return result;
    }

    //删除顾客
    @Override
    public Boolean deleteCus(List<Integer> ids) {
        ids.forEach((id) ->{
            cusMapper.deleteCus(id);
        });
        return true;
    }
}
