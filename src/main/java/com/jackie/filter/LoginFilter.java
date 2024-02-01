package com.jackie.filter;

import com.alibaba.fastjson.JSONObject;
import com.jackie.pojo.Result;
import com.jackie.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")  //servlet服务，需在SpringApp中打开servlet组件检测
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Filter开始工作...");
        Filter.super.init(filterConfig);
    }

    @Override   //校验，判断放行与否
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //下转，为了获得http请求路径
        HttpServletRequest request = (HttpServletRequest) servletRequest;       //ServletRequest是HttpServletRequest他爸
        HttpServletResponse response = (HttpServletResponse) servletResponse;   //response同理，这边是一个下转类型
        //获得路径
        String path = request.getRequestURL().toString();
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH");
        response.setHeader("Access-Control-Allow-Headers","Content-Type,Access-Control-Allow-Origin,WithCredentials,Token, Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        //处理路径请求
        log.info("接收到请求：{}",path);
        //判断前端想要的操作
        if(path.contains("login")){   //前端登录
            filterChain.doFilter(request,response);
            return ;
        }else if(path.contains("checkState")){  //前端意图校验jwt
            filterChain.doFilter(request,response);
            return ;
        }else if(path.contains("jpg") || path.contains("png") || path.contains("svg")){ //非登录用户意图访问本地文件
            filterChain.doFilter(request,response);
            return ;
        }else if(request.getMethod().equals("OPTIONS")){    //axios校验跨域链接可行性
            filterChain.doFilter(request,response);
            return ;
        } else if (path.contains("loadAD")) {   //非登录用户访问广告图片列表
            filterChain.doFilter(request,response);
            return ;
        } else if (path.contains("register")) { //非登录用户提交注册信息
            filterChain.doFilter(request,response);
            return ;
        } else if (path.contains("deptList")){  //注册请求调取部门列表
            filterChain.doFilter(request,response);
            return ;
        } else if (path.contains("chat")) {
            filterChain.doFilter(request,response);
            return ;
        }

        //不登陆、校验令牌
        String jwt = request.getHeader("Token");
        //无令牌
        if(jwt == null || jwt.isEmpty()){
            log.info("空令牌，拒绝执行");
            Result errorMsg = Result.error("请先登录");
            String jsonMsg = JSONObject.toJSONString(errorMsg);
            response.getWriter().write(jsonMsg);     //手动输出流
            return ;
        }
        //有令牌
        try {
            JwtUtils.parseJwt(jwt);
        }catch (Exception ex){
            ex.printStackTrace();
            Result errorMsg = Result.error("登录状态异常，请重新登录");
            String jsonMsg = JSONObject.toJSONString(errorMsg);
            response.getWriter().write(jsonMsg);    //手动输出流
            log.info("非法令牌登录，已驳回");
            return ;
        }
        //全部情况校验通过，放行
        log.info("令牌：{}已通过",jwt);
        filterChain.doFilter(request,response);
        return ;
    }

    @Override
    public void destroy() {
        log.info("Filter已销毁");
        Filter.super.destroy();
    }
}
