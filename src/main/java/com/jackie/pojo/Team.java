package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    private Integer teamId;
    private String name;
    private String teamImage;
    private String description;
    private Integer maxNum;
    private Integer joinNum;
    private LocalDateTime expireTime;
    private Integer creatorId;
    private Integer userId;
    private Short status;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Short isDelete;
}
