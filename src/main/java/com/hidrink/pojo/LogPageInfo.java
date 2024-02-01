package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogPageInfo {
    Integer pageNow;
    Integer pageSize;
    Long totalSize;
    List<OperateLog> tableData;
}
