package com.hidrink.service;

import com.hidrink.pojo.Login;
import com.hidrink.pojo.Register;

public interface LoginService {

    Login login(Login loginInfo);

    Boolean register(Register registerData);
}
