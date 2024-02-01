package com.hidrink.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;                 /*订单ID*/
    private Integer orderNo;            /*订单号*/
    private Short status;               /*订单处理结果：0-已撤销，1-待接单处理，2-处理完毕*/
    private String note;                /*订单备注*/
    private Integer cusId;              /*用户ID*/
    private Integer goodsId;            /*商品ID*/
    private Integer purchaseVolume;     /*商品购买量*/
    private LocalDateTime createTime;   /*订单创建时间*/
    private LocalDateTime updateTime;   /*订单修改时间*/

}
