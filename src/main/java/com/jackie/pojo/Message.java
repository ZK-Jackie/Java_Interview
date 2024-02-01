package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Integer messageId;
    private Integer sendUserId;
    private Integer receiveUserId;
    private Integer teamId;
    private String content;
    private LocalDateTime sendTime;
    private Short receiveType;
    private Short sendType;
    private LocalDateTime readTime;
    private Integer type;
    private LocalDateTime cancelTime;
    private Short isCancel;
    private Short isRead;
}
