package com.jackie.mapper;

import com.jackie.pojo.Login;
import com.jackie.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.swing.*;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
    User selectUserByLoginData(Login loginData);

    @Select("select user_id, username, avatar_url, gender, profile from user where username = #{username}")
    User selectUserByName(String username);

    @Select("select * from user where user_id = #{userId}")
    User selectUserById(Integer userId);

    @Select("select avatar_url from user where user_id = #{userId}")
    String selectAvatarUrlById(Integer userId);
}
