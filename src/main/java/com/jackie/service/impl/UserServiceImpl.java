package com.jackie.service.impl;

import com.jackie.mapper.UserMapper;
import com.jackie.pojo.User;
import com.jackie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User searchUser(String username) {
        return userMapper.selectUserByName(username);
    }
}
