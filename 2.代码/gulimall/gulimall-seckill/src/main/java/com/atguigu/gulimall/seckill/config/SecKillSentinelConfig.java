package com.atguigu.gulimall.seckill.config;

import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import org.springframework.context.annotation.Configuration;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.fastjson.JSON;

import org.springframework.context.annotation.Configuration;
/**
 * <p>Title: SecKillSentinelConfig</p>
 * Description：配置请求被限制以后的处理器
 * date：2020/7/10 13:47
 */
@Configuration
public class SecKillSentinelConfig {
    public SecKillSentinelConfig(){
        WebCallbackManager.setUrlBlockHandler((request, response, exception) -> {
            R error = R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(), BizCodeEnume.TO_MANY_REQUEST.getMsg());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(JSON.toJSONString(error));
        });
    }
}
