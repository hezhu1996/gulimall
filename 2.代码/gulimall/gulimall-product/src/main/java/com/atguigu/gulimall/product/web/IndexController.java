package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

//页面跳转
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //1. 查出所有1级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();

        //2.Model 发送信息
        model.addAttribute("categorys", categoryEntities);

        //* 视图解析器进行拼串
        // classpath:/templates/(前缀) + 返回值 + .html(后缀)
        return "index";
    }

    //web 2.得到二级、三级子分类
    @ResponseBody
    @RequestMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatlogJson() {

        Map<String, List<Catelog2Vo>> map = categoryService.getCatelogJson();

        return map;
    }

}
