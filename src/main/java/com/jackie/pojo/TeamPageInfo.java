package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamPageInfo {
    Integer id;
    String name;
    Integer pageNow;
    Long totalSize;
    List<Team> tableData;
}
