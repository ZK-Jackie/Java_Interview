package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Register {
    private Short role;
    private String username;
    private String password;
    private Integer age;
    private Short gender;
    private Integer deptId;
    private Boolean isSuccess;
    private String message;
    private LocalDateTime entryTime;        /*入职时间*/
    private LocalDateTime createTime;   /*数据条目创建时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
