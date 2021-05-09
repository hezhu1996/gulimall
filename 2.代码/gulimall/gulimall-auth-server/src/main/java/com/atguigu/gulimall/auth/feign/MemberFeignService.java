package com.atguigu.gulimall.auth.feign;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.vo.SocialUser;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimall-member")
public interface MemberFeignService {
    //认证服务，远程调用注册会员
    @PostMapping("member/member/register")
    public R register(@RequestBody UserRegisterVo userRegisterVo);

    //LoginController：远程调用登录服务
    @PostMapping("member/member/login")
    public R login(@RequestBody UserLoginVo vo);

    //社交账户Auth2登录
    @PostMapping("member/member/oauth2/login")
    public R login(@RequestBody SocialUser socialUser);
}
