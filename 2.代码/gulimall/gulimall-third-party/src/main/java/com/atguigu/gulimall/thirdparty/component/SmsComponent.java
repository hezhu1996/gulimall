package com.atguigu.gulimall.thirdparty.component;

import com.atguigu.gulimall.thirdparty.util.HttpUtils;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class SmsComponent {

    public void sendSmsCode(String phone, String code) {
        String host = "https://msg.market.alicloudapi.com";
        String path = "/msg";
        String method = "POST";
        String appcode = "9d6b455f62f14111bba5689186c27b1d";
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "APPCODE " + appcode);

        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();
        bodys.put("message", "您的验证码是"+ code +"。如非本人操作，请忽略本短信");
        bodys.put("mobile", phone);
        bodys.put("mouldno", "44CAD037");
        bodys.put("title", "云码通");

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





















