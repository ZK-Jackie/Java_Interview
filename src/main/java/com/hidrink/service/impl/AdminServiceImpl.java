package com.hidrink.service.impl;

import com.hidrink.mapper.AdminMapper;
import com.hidrink.pojo.Admin;
import com.hidrink.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    public AdminMapper adminMapper;

    @Override
    public Admin getInfo(Integer id) {
        return adminMapper.getInfoById(id);
    }
}
