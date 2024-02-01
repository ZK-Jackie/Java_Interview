package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private Integer id;
    private String username;    /*校验账号*/
    private String password;    /*校验密码*/
    private Short role;       /*登录角色*/
    private Boolean isSuccess;       /*登录成功/失败*/
    private String token;
}
