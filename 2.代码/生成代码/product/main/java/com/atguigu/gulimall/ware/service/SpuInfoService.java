package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 20:50:01
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

