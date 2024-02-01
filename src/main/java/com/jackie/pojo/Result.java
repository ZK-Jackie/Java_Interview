package com.jackie.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String message;
    private Object data;

    /*方法是静态的，但是整个对象和对应的数据不是静态的，都是可以根据需要随时创建使用的*/
    public static Result success(){
        return new Result(1,"success",null);
    }

    public static Result success(Object data){
        return new Result(1,"success",data);
    }

    public static Result error(String msg){
        return new Result(0,msg,null);
    }
}
