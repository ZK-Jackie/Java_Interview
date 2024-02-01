package com.hidrink.mapper;

import com.hidrink.pojo.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO orders (cus_id, goods_id, purchase_volume, create_time) VALUES (#{cus_id}, #{goods_id}, #{purchase_volume}, #{create_time})")
    Boolean insertOrder(Order order);

    @Update("UPDATE orders SET cus_id=#{cus_id}, goods_id=#{goods_id}, purchase_volume=#{purchase_volume}, create_time=#{create_time} WHERE id=#{id}")
    Boolean updateOrder(Order order);

    @Delete("DELETE FROM orders WHERE id=#{id}")
    Boolean deleteOrderById(Integer id);

    @Select("SELECT * FROM orders WHERE id=#{id}")
    Order selectOrderById(Integer id);

    @Select("SELECT * FROM orders")
    Object selectAllOrders(Object myOrder);

}