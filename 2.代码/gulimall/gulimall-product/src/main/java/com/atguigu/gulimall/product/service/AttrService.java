package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //1.保存"规格参数"中的所有数据 + attrGroupId
    void saveAttr(AttrVo attr);

    // 获取分类"规格参数" + 分页查询
    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    //07、查询属性详情
    AttrRespVo getAttrInfo(Long attrId);

    //07_2 修改规格数据
    void updateAttr(AttrVo attr);

    //10、获取属性分组的关联的所有属性
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    //12、删除属性与分组的关联关系
    void deleteRelation(AttrGroupRelationVo[] vos);

    //13、查询：获取"分组"没有关联的其他属性
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    //1.5.3 attrEntity -> search_type
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}

