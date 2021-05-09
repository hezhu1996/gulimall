package com.atguigu.gulimall.auth.vo;

import lombok.Data;

@Data
public class UserLoginVo {
    //登录账户
    private String loginacct;
    //登录密码
    private String password;
}
