package com.atguigu.guilimall.search.controller;

import com.atguigu.guilimall.search.service.MallSearchService;
import com.atguigu.guilimall.search.vo.SearchParam;
import com.atguigu.guilimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    /**
     * 页面提交查询请求参数 -> 对象
     * @param param
     * @return
     */
    //1.页面查询参数 -> es检索商品 -> 返回商城检索结果
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {

        // 获取路径原生的查询属性
        param.set_queryString(request.getQueryString());
        SearchResult result = mallSearchService.search(param);

        model.addAttribute("result", result);
        return "list";
    }
}
