package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {

    private Integer id;                 /*商品ID*/
    private String name;                /*商品名称*/
    private Integer goodsNum;           /*商品数量*/
    private String introduction;        /*商品简介*/
    private Double price;               /*商品价格*/
    private Long stock;                 /*商品库存*/
    private Integer salesVolume;        /*商品销售量*/
    private LocalDateTime createTime;   /*商品创建时间*/
    private LocalDateTime updateTime;   /*商品修改时间*/
    private String urlPicture;          /*商品图片*/

}
