package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private String username;
    private String userAccount;
    private String avatarUrl;
    private Short gender;
    private String password;
    private String phone;
    private String email;
    private String profile;
    private Short userStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Short isDelete;
    private Short userRole;
}
