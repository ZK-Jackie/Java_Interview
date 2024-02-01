package com.hidrink.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPageInfo {
    String name;                /*商品名称*/
    Integer pageNow;            /*商品当前页码*/
    Integer pageSize;           /*当前页面最大数据量*/
    Integer priceOrder;         /*价格升序降序判断*/
    Integer minPrice;           /*最低价格*/
    Integer maxPrice;           /*最高价格*/
    List<Goods> tableData;      /*表格数据*/

}
