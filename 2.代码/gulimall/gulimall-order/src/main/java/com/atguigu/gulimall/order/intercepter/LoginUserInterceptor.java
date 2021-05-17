package com.atguigu.gulimall.order.intercepter;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.vo.MemberRsepVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRsepVo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        // 这个请求直接放行
        boolean match = new AntPathMatcher().match("/order/order/status/**", uri);
        if(match){
            return true;
        }

        //获取登录用户
        HttpSession session = request.getSession();
        MemberRsepVo memberRsepVo = (MemberRsepVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        //用户已登录，放行
        if(memberRsepVo != null){
            threadLocal.set(memberRsepVo);
            return true;
        }
        // 没登陆就去登录
        else{
            session.setAttribute("msg", AuthServerConstant.NOT_LOGIN);
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }
    }
}
