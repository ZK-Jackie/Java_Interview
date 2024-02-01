package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpPageInfo {
    Integer pageNow;
    Integer pageSize;
    Long totalSize;
    String name;
    String startDate;
    String endDate;
    List<Emp> tableData;
}
