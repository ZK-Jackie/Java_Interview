package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AD {
    private Integer id;
    private String img;
    private String url;
    private String intro;
    private LocalDateTime createTime;   /*数据条目创建时间*/
    private LocalDateTime updateTime;   /*数据条目修改时间*/
}
