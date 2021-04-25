package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    //3.1 远程调用成功,修改当前spu状态
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
