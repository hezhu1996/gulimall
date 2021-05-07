package com.atguigu.gulimall.product.component;


import com.atguigu.common.utils.HttpUtils;
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


// @Data
// @ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
// @Component
// public class SmsComponent {
//
//     public String sendSmsCode(String phone, String code){
//         String method = "GET";
//         Map<String, String> headers = new HashMap<String, String>();
//         //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//         headers.put("Authorization", "APPCODE " + "541707ddc9c8463eb9336c3bfc0624b3");
//         Map<String, String> querys = new HashMap<String, String>();
//         querys.put("code", code);
//         querys.put("phone", phone);
//         querys.put("skin", "1");
//         querys.put("sign", "1");
//         HttpResponse response = null;
//         try {
//             response = HttpUtils.doGet("https://fesms.market.alicloudapi.com", "/sms/", method, headers, querys);
//             //获取response的body
//             if(response.getStatusLine().getStatusCode() == 200){
//                 return EntityUtils.toString(response.getEntity());
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return "fail_" + response.getStatusLine().getStatusCode();
//     }
// }