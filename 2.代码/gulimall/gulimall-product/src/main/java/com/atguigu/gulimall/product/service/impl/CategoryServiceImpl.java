package com.atguigu.gulimall.product.service.impl;

import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    //1 查出所有分类并组装成树形结构
    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2.组装父子的树形结构
        //2.1 找到所有一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter((categoryEntity) -> {
            return categoryEntity.getParentCid() == 0;
        }).map((menu) -> {
            //1.递归找到子菜单
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            //2.菜单排好序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }

    //递归查询所有菜单子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid().equals(root.getCatId());
        }).map(categoryEntity -> {
            //1.递归找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2.菜单排好序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

    //2 删除：检查当前删除菜单，是否被别的地方引用
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.检查是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    //根据catId，查询CatelogPath完整路径
    //eg.[2, 25, 225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        //1.path为最终路径数组
        List<Long> paths = new ArrayList<>();
        //2.递归寻找父路径
        findParentPath(catelogId, paths);

        Collections.reverse(paths);

        return paths.toArray(new Long[0]);
    }

    private void findParentPath(Long catelogId, List<Long> paths) {
        //1.收集当前节点id
        paths.add(catelogId);
        //2.循环查找父节点
        CategoryEntity category = this.getById(catelogId);
        while(category.getParentCid() != 0){
            Long parentCid = category.getParentCid();
            paths.add(parentCid);
            category = this.getById(parentCid);
        }
    }

    //1.因为有categoryRelation的冗余存储，不能光改品牌表，所有冗余存储都要更新
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        //1.更新当前"分类表"中的数据'
        baseMapper.updateById(category);
        //2.更新其他存有"分类名的表"
        //2.1 更新 分类关联 表
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

}







