package com.atguigu.guilimall.search.service;

import com.atguigu.guilimall.search.vo.SearchParam;
import com.atguigu.guilimall.search.vo.SearchResult;

public interface MallSearchService {
    //1.页面查询参数 -> es检索商品 -> 返回商城检索结果
    SearchResult search(SearchParam param);
}
