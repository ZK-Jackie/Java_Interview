package com.hidrink.mapper;

import com.hidrink.pojo.Cus;
import com.hidrink.pojo.CusPageInfo;
import com.hidrink.pojo.Login;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CusMapper {

    @Select("select * from cus where username=#{username} and password=#{password}")
    Cus checkLogin(Login loginInfo);

    @Select("select count(*) from cus")
    Integer countCus();

    @Select("select count(*) from cus where gender = 0;")
    Integer countMale();

    @Select("select count(*) from cus where gender = 1;")
    Integer countFemale();

    @Select("select count(*) from cus where DATE(create_time) = CURDATE();")
    Integer countTodayRegister();

    @Select("select * from cus")
    List<Cus> getCusPage(CusPageInfo cusPageInfo);

    @Update("update cus set username=#{username}, password=#{password}, gender=#{gender}, birthday=#{birthday}, update_time=#{updateTime} where id = #{id}")
    Boolean updateCusId(Cus cus);

    List<Cus> getCus(CusPageInfo cusPageInfo);

    @Insert("insert cus(id, username, password, img, gender, birthday, create_time, update_time) values (id=#{id}, username=#{username}, password=#{password}, img=#{img}, gender=#{gender}, birthday=#{birthday}, create_time=#{createTime}, update_time=#{updateTime})")
    Boolean insertCus(Cus cus);

    @Delete("delete from cus where id = #{id}")
    Boolean deleteCus(Integer id);

}
