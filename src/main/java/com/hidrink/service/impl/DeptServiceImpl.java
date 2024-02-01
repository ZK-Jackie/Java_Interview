package com.hidrink.service.impl;

import com.hidrink.mapper.DeptMapper;
import com.hidrink.pojo.Dept;
import com.hidrink.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    public DeptMapper deptMapper;

    @Override
    public List<Dept> getDept() {
        return deptMapper.getAllDept();
    }

    @Override
    public boolean updateDeptByID(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        return deptMapper.updateDeptByID(dept);
    }

    @Override
    public Map<Integer, String> getDeptList() {
        HashMap<Integer, String> map = new HashMap<>();
        List<Integer> ids = deptMapper.getIds();
        List<String> names = deptMapper.getDepts();
        map = IntStream.range(0,ids.size()).collect(HashMap::new, (m,i) -> m.put(ids.get(i), names.get(i)),(m,n) -> {});
        return map;
    }

    @Override
    public boolean addDept(Dept dept) {
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        return deptMapper.insertDept(dept);
    }

    @Override
    public boolean deleteDept(Integer id) {
        return deptMapper.deleteDept(id);
    }
}
