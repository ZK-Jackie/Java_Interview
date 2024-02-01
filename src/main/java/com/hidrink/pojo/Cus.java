package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cus {
    private Integer id;                 /*顾客ID*/
    private Short role;                 /*登录人角色_2*/
    private String username;            /*用作账号*/
    private String password;
    private String img;                 /*头像url或uid*/
    private Integer gender;
    private Date birthday;
    private LocalDateTime createTime;   /*数据条目创建时间，可以当作注册时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
