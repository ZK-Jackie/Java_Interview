package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private Integer userId;
    private String username;    /*校验账号*/
    private String password;    /*校验密码*/
    private String url;         /*用户头像*/
    private Boolean isSuccess;  /*登录成功/失败*/
    private String token;
}
