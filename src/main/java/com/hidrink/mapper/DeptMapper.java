package com.hidrink.mapper;

import com.hidrink.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DeptMapper {

    @Select("select * from dept")
    List<Dept> getAllDept();

    @Update("update dept set name=#{name}, intro=#{intro}, update_time=#{updateTime} where id=#{id}")
    boolean updateDeptByID(Dept dept);

    HashMap<Integer, String> getDeptList();

    @Select("select id from dept")
    List<Integer> getIds();

    @Select("select name from dept")
    List<String> getDepts();

    @Insert("insert dept(name,intro,establish_time,create_time,update_time) values(#{name}, #{intro}, #{establishTime} ,#{createTime}, #{updateTime})")
    boolean insertDept(Dept dept);

    @Delete("delete from dept where id = #{id}")
    boolean deleteDept(Integer id);
}
