package com.hidrink.service;

import com.hidrink.pojo.Dept;

import java.util.List;
import java.util.Map;

public interface DeptService {
    List<Dept> getDept();

    boolean updateDeptByID(Dept dept);

    Map<Integer, String> getDeptList();

    boolean addDept(Dept dept);

    boolean deleteDept(Integer id);
}
