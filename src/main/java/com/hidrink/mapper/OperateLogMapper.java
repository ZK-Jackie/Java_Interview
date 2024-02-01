package com.hidrink.mapper;

import com.hidrink.pojo.LogPageInfo;
import com.hidrink.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {
    //注意mybatis中不要随便添加命令末尾的分号

    @Insert("insert into operate_log (operate_user, operate_role, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateUser},#{operateRole}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime})")
    void insert(OperateLog log);

    @Select("select operate_user, operate_role, operate_time, class_name, method_name, method_params,cost_time from operate_log")
    List<OperateLog> select(LogPageInfo info);
}
