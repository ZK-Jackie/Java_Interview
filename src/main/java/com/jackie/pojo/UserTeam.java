package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTeam {
    private Integer id;
    private Integer userId;
    private Integer teamId;
    private LocalDateTime joinTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Short isDelete;
}
