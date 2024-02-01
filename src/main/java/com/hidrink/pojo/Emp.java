package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp {
    private Integer id;                 /*员工ID*/
    private Short role;                 /*登录人角色_1*/
    private String username;            /*用作账号*/
    private String password;
    private String img;                 /*头像url或uid*/
    private Short gender;
    private Integer deptId;             /*员工部门ID*/
    private LocalDateTime entryTime;        /*入职时间*/
    private LocalDateTime createTime;   /*数据条目创建时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
