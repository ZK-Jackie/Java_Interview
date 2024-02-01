package com.hidrink.service;

import com.hidrink.pojo.Emp;
import com.hidrink.pojo.EmpPageInfo;

import java.util.List;

public interface EmpService {

    EmpPageInfo getEmp(EmpPageInfo empPageInfo);

    boolean updateEmp(Emp emp);

    boolean addEmp(Emp emp);

    boolean deleteEmps(List<Integer> ids);

    boolean uploadFile(Integer id, String img);

    Emp getInfo(Integer id);

}
