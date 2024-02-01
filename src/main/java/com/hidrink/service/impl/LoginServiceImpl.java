package com.hidrink.service.impl;

import com.hidrink.mapper.CusMapper;
import com.hidrink.pojo.*;
import com.hidrink.mapper.AdminMapper;
import com.hidrink.mapper.CusMapper;
import com.hidrink.mapper.EmpMapper;
import com.hidrink.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    public AdminMapper adminMapper;
    @Autowired
    public EmpMapper empMapper;
    @Autowired
    public CusMapper cusMapper;

    @Override
    public Login login(Login loginInfo) {
        if(loginInfo.getRole() == 2){
            Cus cus = cusMapper.checkLogin(loginInfo);
            if(cus != null){
                loginInfo.setRole((short)2);
                loginInfo.setId(cus.getId());
                loginInfo.setIsSuccess(true);
            }else{
                loginInfo.setIsSuccess(false);
            }
            return loginInfo;
        }else{
            Admin admin = adminMapper.checkLogin(loginInfo);
            if(admin != null){
                loginInfo.setRole((short)0);
                loginInfo.setId(admin.getId());
                loginInfo.setIsSuccess(true);
                return loginInfo;
            }
            Emp emp = empMapper.checkLogin(loginInfo);
            if(emp != null){
                loginInfo.setRole((short)1);
                loginInfo.setId(emp.getId());
                loginInfo.setIsSuccess(true);
                return loginInfo;
            }
        }
        loginInfo.setIsSuccess(false);
        return loginInfo;
    }

    @Override
    public Boolean register(Register registerData) {
        if(registerData.getRole() == 1){
            registerData.setCreateTime(LocalDateTime.now());
            registerData.setUpdateTime(LocalDateTime.now());
            return empMapper.insertNewEmp(registerData);
        }
        return false;
    }
}
