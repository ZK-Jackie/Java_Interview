package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    private Integer id;                 /*部门ID*/
    private String name;                /*部门名称*/
    private Date establishTime;         /*成立时间*/
    private Integer empNum;             /*部门人数*/
    private String intro;               /*部门简介*/
    private LocalDateTime createTime;   /*数据条目创建时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
