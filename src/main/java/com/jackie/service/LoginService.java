package com.jackie.service;

import com.jackie.pojo.Login;
import com.jackie.pojo.Register;
import com.jackie.pojo.User;

public interface LoginService {
    User login(Login loginData);

    void register(Register registerData);

    String getAvatarUrl(Integer userId);
}
