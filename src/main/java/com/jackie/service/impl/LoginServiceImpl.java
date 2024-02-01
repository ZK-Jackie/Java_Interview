package com.jackie.service.impl;

import com.jackie.mapper.UserMapper;
import com.jackie.pojo.Login;
import com.jackie.pojo.Register;
import com.jackie.pojo.User;
import com.jackie.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    public UserMapper userMapper;

    @Override
    public User login(Login loginData) {
        User user = userMapper.selectUserByLoginData(loginData);
        if(user != null && user.getUserStatus() == 0){
            loginData.setIsSuccess(true);
        }
        return user;
    }

    @Override
    public void register(Register registerData) {

    }

    @Override
    public String getAvatarUrl(Integer userId) {
        return userMapper.selectAvatarUrlById(userId);
    }
}
