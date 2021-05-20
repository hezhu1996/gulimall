package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

//页面跳转，直接Controller就可以
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    //1.展示当前sku的详情
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        //1.1 查询sku详细内容
        SkuItemVo skuItemVo = skuInfoService.item(skuId);

        model.addAttribute("item", skuItemVo);

        return "item";
    }
}




























