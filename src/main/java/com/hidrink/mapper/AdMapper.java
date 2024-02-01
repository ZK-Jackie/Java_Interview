package com.hidrink.mapper;

import com.hidrink.pojo.AD;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdMapper {
    @Select("select * from ad")
    List<AD> getAllAD();

    @Update("update ad set img = #{img} and url = #{url} and intro = #{intro} and update_time = #{updateTime} where id = #{id}")
    boolean updateADById(AD ad);

    @Insert("insert into ad(img,url,intro,update_time) values(#{img},#{url},#{intro},#{updateTime})")
    boolean insertAD(AD ad);

    @Delete("delete from ad where id = #{id}")
    boolean deleteADById(Integer id);

    @Update("update ad set img = #{img}, update_time = #{updateTime} where id = #{id}")
    boolean updateImgById(Integer id, String img, LocalDateTime updateTime);

    @Select("select max(id) from ad")
    Integer getLatest();

    @Select("select img from ad")
    List<AD> getImg();
}
