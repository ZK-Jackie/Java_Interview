package com.hidrink.mapper;

import com.hidrink.pojo.Emp;
import com.hidrink.pojo.EmpPageInfo;
import com.hidrink.pojo.Login;
import com.hidrink.pojo.Register;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface EmpMapper {
    //注意：mapper方法不可重载

    @Select("select * from emp where username=#{username} and password=#{password}")
    Emp checkLogin(Login login);

    //动态映射
    List<Emp> getEmp(EmpPageInfo pageNum);

    boolean updateEmpById(Emp emp);

    @Insert("insert into emp(username, password, gender, dept_id, entry_time, create_time, update_time) " +
            "values (#{username}, #{password}, #{gender}, #{deptId}, #{entryTime}, #{createTime}, #{updateTime})")
    boolean insertEmp(Emp emp);

    boolean deleteEmp(List<Integer> ids);

    @Update("update emp set img = #{img} where id = #{id}")
    boolean updateImgById(Integer id,String img);

    @Insert("insert into emp(username,password,gender,dept_id,entry_time,create_time,update_time)" +
                "values (#{username}, #{password}, #{gender}, #{deptId}, #{entryTime}, #{createTime}, #{updateTime})")
    boolean insertNewEmp(Register registerData);

    @Select("select * from emp where id = #{id}")
    Emp getInfoById(Integer id);
}
