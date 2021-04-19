package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 19、新增商品
    void saveSpuInfo(SpuSaveVo spuInfo);

    //保存基本信息
    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    //18、spu检索
    PageUtils queryPageByCondition(Map<String, Object> params);
}

