package com.atguitu.gulimall.cart.interceptor;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.constant.CartConstant;
import com.atguigu.common.vo.MemberRsepVo;
import com.atguitu.gulimall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;


public class CartInterceptor implements HandlerInterceptor {

    //thread local
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        //1.用户登陆，在session获得 username, id等用户信息
        HttpSession session = request.getSession();
        MemberRsepVo user = (MemberRsepVo) session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (user != null){
            // 用户登陆了
            userInfoTo.setUsername(user.getUsername());
            userInfoTo.setUserId(user.getId());
        }
        //2.用户未登录，cookie 中得到 user-key 信息
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            // 在 cookies 中搜索，如果本地浏览器中有 user-key，则设置当前临时用户 userInfoTo 的 user-key
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                //设置user-key
                if(name.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //3.如果没有临时用户 则分配一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString().replace("-","");
            userInfoTo.setUserKey("Maple-" + uuid);
        }
        threadLocal.set(userInfoTo);
        return true;

    }


    /**
     * 执行完毕之后分配临时用户让浏览器保存
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        UserInfoTo userInfoTo = threadLocal.get();
        //当前没有临时用户，才向cookie中存储
        if(!userInfoTo.isTempUser()){
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            // 设置这个cookie作用域 过期时间
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIME_OUT);
            response.addCookie(cookie);
        }
    }

}























