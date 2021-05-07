package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.BrandVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //1.因为有categoryRelation的冗余存储，不能光改品牌表，所有荣誉存储都要更新
    void updateDetail(BrandEntity brand);

    //search 面包屑导航，调用远程信息
    List<BrandVo> getBrandByIds(List<Long> brandIds);
}

