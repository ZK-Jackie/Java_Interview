package com.hidrink.mapper;

import com.hidrink.pojo.Admin;
import com.hidrink.pojo.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("select * from admins where username=#{username} and password=#{password}")
    Admin checkLogin(Login login);

    @Select("select * from admins where id = #{id}")
    Admin getInfoById(Integer id);
}
