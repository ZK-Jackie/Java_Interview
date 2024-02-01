package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    private Integer id;                 /*管理员ID*/
    private Short role;                 /*登录人角色_0*/
    private String username;            /*用作账号*/
    private String password;
    private String name;                /*使用人的名字*/
    private LocalDateTime createTime;   /*数据条目创建时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
