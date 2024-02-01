package com.hidrink.mapper;

import com.hidrink.pojo.Goods;
import com.hidrink.pojo.GoodsPageInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.time.LocalDateTime;

@Mapper
public interface GoodsMapper {

    @Insert("INSERT INTO goods(name, introduction, price, stock, sales_volume ,create_time, update_time) " +
            "VALUES(#{name}, #{introduction}, #{price}, #{stock}, #{sales_volume}, #{create_time}, #{update_time})")
    void insertGoods(Goods goods);

    @Update("UPDATE goods SET name = #{name}, introduction = #{introduction}, price = #{price}, " +
            "stock = #{stock}, sales_volume = #{sales_volume},create_time = #{create_time} , update_time = #{update_time} WHERE id = #{id}")
    void update(Goods goods);

    @Delete("DELETE FROM goods WHERE id = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM goods WHERE id = #{id}")
    List<Goods> findById(Integer id);

    List<Goods> getGoods();

//    List<Goods> findAll();

}

