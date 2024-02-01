package com.hidrink.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hidrink.mapper.EmpMapper;
import com.hidrink.pojo.Emp;
import com.hidrink.pojo.EmpPageInfo;
import com.hidrink.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    public EmpMapper empMapper;

    @Override
    public EmpPageInfo getEmp(EmpPageInfo empPageInfo) {  /*分页查询与条件筛选*/
        //1.设置分页参数
        PageHelper.startPage(empPageInfo.getPageNow(), empPageInfo.getPageSize());
        //2.将查询结果交给Page泛型
        List<Emp> data = empMapper.getEmp(empPageInfo);
        //3.包装数据
        empPageInfo.setTableData(((Page<Emp>) data).getResult());
        empPageInfo.setTotalSize(((Page<Emp>) data).getTotal());
        //4.返回controller
        return empPageInfo;
    }

    @Override
    public boolean updateEmp(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateEmpById(emp);
        return true;
    }

    @Override
    public boolean addEmp(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insertEmp(emp);
        return true;
    }

    @Override
    public boolean deleteEmps(List<Integer> ids) {
        empMapper.deleteEmp(ids);
        return true;
    }

    @Override
    public boolean uploadFile(Integer id, String img) {
        empMapper.updateImgById(id,img);
        return true;
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getInfoById(id);
    }


}
