package com.atguigu.gulimall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import com.atguigu.gulimall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000117659816";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKj9RSz2TZx5jtv8L03wj5/J+75ABPgh+2zSasT/1QdxhLcViP9hlo32FLvH1V3aKFtgAA7H65t3UPyvFmBjwRP6qN6uc1hxsis98l4c7ygPs8hGBgshzyX1l5OMF8/i6pA2MN5XrQp0l+oqH/QdG2ilvzcUnxvgPspSV1E4C+w+Gcqx+0SgBcPNKZW6ryaY77PnAu5Mz2loLajzGYQtQiSdJkbqmFJ59JUQrYlzHyWkOSOPq3HShGFrA5n3CcI90q/xcMko9ZjDP8t21joKwU2rZ0pfAFOMMhoDNR3efJu5dkCxmAhf8xMYMVVy7ZVo3w3ZxgD+TeXDRL8tlQKDLxAgMBAAECggEACi0H7dD3WhsTexpjKfKbSSipXnYfI7ettpIYbf0t4FPuv9OiKmhrc008awB7v+uX/PPhyi+UGvWDLla8Jb79eFLRpryCoGa8sm69S53qwdqBGO+r2E4cXBxuiL2HUDOmn5j1FFcvDtXKSveMqB9gVGSS3i0KviszRJTYTGHTSa8jWtT9V9lWMS7b4bh6A0c6qHxLhcYQQxP7zhcSTWpvSZqp6t9h2IrLKVpac7h7PMCTCAyQ8qFsmMM3mYM5bv9pVzTRhlZPiFg3qTnVr54Gt8HmN/MWduERxMVqx1jSyCSJV1drgFrNo8sMO7YCAAU0In7jiu/qtmjlF3AL54l9sQKBgQDDb+Nm3omVJAYJlwhKURwNQ5YMOKUmNUbQpyxcgO9mVqsOPzLUiO8k4F9jze/9xFvR5XcmcTjWoSM3LrMmr1giKh6QwbOoAqjiWQXSHJJzEZrNHHx3WyjFivigcxSDvROvLnozMRBs/W2NjR2tbROF5lHgNLMGEJ4Y+HAT7Lta7QKBgQC1gADI6B50jaIeImE4idVbAsN2oLBpJ4ctwWhzFy4os1C1s98VuHoF6ulCTBRZuAe14i46puOrJucB3CA0UX3KSgD7/fe//2enTcRH2kx9Ubllp+BeBKsplaJU+HHw1+AMmOITKTMw8MN6n+GLvXiVs0LPfIFS2Ga+R81FYkmDlQKBgQCX2pZM1dj9QzJVvT65z/AF9l/eOx1HZNoTQfHcJmrdj7X1qfFq5sM6WgSEXf19plbZ7nv4KL1LN4VPWT/mbRQD58o8G+XmQ7rJtPYq3fyE4Ebx3JJxgylY5LRupzxnaEClO23D3PmwVfILrHxExp4GyoEPvqLjsYHGUTN0p9qjuQKBgQCAnAZyVoCWKHJzFft8G7cyeV/WJIE80gSouH7x5sAPmRic2KtAcesNXGsTTmOxO4VDLdWCPBXhhnx0xe2QBRq0iAHwjgLLawig9asbGyXOO2W7Ed0b4gUDP4O64B2lAuNBKegwmaYrjL+Hmdkxsv3KXi7+ZYQiFzOeq7RZ8Q6UjQKBgGobio3nDTYfeUfLnOG2pQgHaxcjUC46suE1mNl0TGUEPkxS6vCy3qcgxGfAf9VHBGa6jvRFs/HefoKayD8hVNyNE8mO/jTdnF40oJ9RQrjzC2UbRb2oStwiLXwxSRZrfJNI22F4pPWFafJtoGU2coPhWBt/RCZ1fVeNir+QSG4Z";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgMh8Cvb475Qz1ArIrI3/dOoHS82YFJamXY06pefWnpTZQ+n2ksFYtUtjbvd4Kg2RZXljOpvN94ltHYCNO17fC9kO5R46xIyi9rF0k5yqzKGcG7PpEhGdWjy/qVh/Jva/JGQ3s7TBNUky0qTtMSUDdboCZ06pRYtx1/Ab3H7HCXRXSUMFDySKmzPMjC6RuySTw+eEzVndOTjNoxV6a/D1G7R924UJe5O7nd2nhbnLYXlVcj1rAQ3sr7Ui8vO+Rp7zrBVk/UAVXwVS7dz76WhTiBfiPYgAkFXXs+gyTB3NSEL6bPohtxJI3kq6VQiOUWJNCnNZnlquln/SwSfxijvwpQIDAQAB";

    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    //"http://order.gulimall.com/payed/notify"
    private  String notify_url = "http://member.gulimall.com/memberOrder.html";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 同步通知，支付成功，一般跳转到成功页
    private  String return_url = "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 自动关单时间
    private String timeout = "15m";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\"" + timeout + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
