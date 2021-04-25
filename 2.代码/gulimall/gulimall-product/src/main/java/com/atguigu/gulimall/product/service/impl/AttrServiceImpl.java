package com.atguigu.gulimall.product.service.impl;

import com.atguigu.common.constant.ProductConstant;
import com.atguigu.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.AttrAttrgroupRelationService;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.AttrGroupRelationVo;
import com.atguigu.gulimall.product.vo.AttrRespVo;
import com.atguigu.gulimall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.AttrDao;
import com.atguigu.gulimall.product.entity.AttrEntity;
import com.atguigu.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationService attrgroupRelationService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrAttrgroupRelationDao relationDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    //保存"规格参数"中的所有数据 + attrGroupId
    //attrGroupService.saveAttr(attrGroup);
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        //1.保存基本信息到 pms_attr 数据库
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.insert(attrEntity);

        //* 只有是 "基本属性" 并且 groupId 有值，才会保存到 "关系表"
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            //2.保存关联关系到 pms_attr_attrgroup_relation
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            //2.1 保存attr_id 和 attr_group_id
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            //2.2 保存到数据库
            attrgroupRelationService.save(relationEntity);
        }
    }

    // 获取分类"规格参数" + 分页查询
    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        //1.查询条件
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();

        // *判断是 "基本属性" 还是 "销售属性"
        // type是base，则attr_type = 1；否则attr_type = 0
        queryWrapper.eq("attr_type", "base".equalsIgnoreCase(type)
                ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()
                : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        //1.1 如果 catelogId 有值
        if(catelogId != 0){
            queryWrapper.eq("catelog_id", catelogId);
        }
        //1.2 如果输入框有值
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            //1.2.1 查找数据库中 "attr_id" 相等，或，模糊查询 "attr_name"
            queryWrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        //1.3 分页查询
        //1.3.1 封装分页参数
        IPage<AttrEntity> getPage = new Query<AttrEntity>().getPage(params);
        //1.3.2 this.baseMapper.selectPage，Mybatis的分页封装信息
        IPage<AttrEntity> page = this.page(getPage, queryWrapper);
        //1.3.3 pageUtils存储分页所有信息
        PageUtils pageUtils = new PageUtils(page);

        //2.将PageUtils中加入AttrRespVo信息
        //2.1得到现有数据
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            //2.2 新建pageUtils，并拷贝现有数据
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //2.3 设置"所属分组 (groupName)"
            //2.3.1 根据 AttrId，查询"关联表"中的 实体"attrRelation"
            QueryWrapper<AttrAttrgroupRelationEntity> relationWrapper = new QueryWrapper<>();
            relationWrapper.eq("attr_id", attrEntity.getAttrId());
            AttrAttrgroupRelationEntity attrRelation = attrgroupRelationService.getOne(relationWrapper);

            //2.3.2如果能在关联表查到相关数据
            if (attrRelation != null && attrRelation.getAttrGroupId() != null) {
                //2.3.3 根据 AttrGroupId 在属性表中查询对应信息
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrRelation.getAttrGroupId());
                //2.3.4 在属性表（pms_attr_group）中查询并设置分组名称
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }

            //2.4 设置"所属分类 (catelogName)"
            //简单一些，因为属性表中有category_id信息
            //2.4.1 根据 catelog_id 在分类表中查询对应信息
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            //2.4.2 如果分类有值，则设置到attrRespVo中
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());

        //3.更新PageUtils的值为attrRespVo
        pageUtils.setList(respVos);

        return pageUtils;
    }

    //07、查询属性详情
    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        //1.查询当前属性详细信息,并复制到AttrRespVo
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity, respVo);

        // * 判断当前是 "基本信息" 才设置 "分组id"
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            //2. 设置"分组信息": attr_id -> attr_group_id -> attr_group_name
            //2.1 根据属性表中attrId，查询relation表中，关于group的信息
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id", attrId);
            //group中的全部信息
            AttrAttrgroupRelationEntity attrgroupRelation = attrgroupRelationService.getOne(wrapper);
            //2.2 如果group_relation不为空，设置其分组id
            if(attrgroupRelation != null){
                Long attrGroupId = attrgroupRelation.getAttrGroupId();
                respVo.setAttrGroupId(attrGroupId);
                //2.3 根据groupId，找到"属性分组"表中的Name信息
                AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
                //2.4 如果 group 不为空,设置其分类名称
                if(attrGroup != null){
                    String attrGroupName = attrGroup.getAttrGroupName();
                    respVo.setGroupName(attrGroupName);
                }
            }
        }


        //3. 设置分类"完整路径"
        Long catelogId = attrEntity.getCatelogId();
        //3.1 得到完整路径
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        //3.2 设置完整路径
        respVo.setCatelogPath(catelogPath);
        //3.3 设置分类名称
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {
            respVo.setCatelogName(categoryEntity.getName());
        }

        return respVo;
    }

    //07_2 修改规格数据
    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        //1.先修改"分类信息"
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.updateById(attrEntity);

        //* 只有 "基本属性" 才更新分组信息
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //2.修改"分组关联"信息：根据attr_id修改其对应的attr_group_id
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            //2.1 向实体中设置新的groupId(对某个attr更改group)
            relationEntity.setAttrId(attr.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            //2.2 update 关联表
            //如果没有则新增
            QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("attr_id", attr.getAttrId());
            //2.2.1 判断relation表中是否有该数据
            int count = attrgroupRelationService.count(queryWrapper);
            if (count > 0) {
                //修改
                UpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("attr_id", attr.getAttrId());
                attrgroupRelationService.update(relationEntity,updateWrapper);
            }
            else {
                //新增
                attrgroupRelationService.save(relationEntity);
            }
        }
    }

    //10、获取分组关联的所有属性; 根据分组id，查询关联的所有基本属性
    //attr_group_id -> attr_id
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        //1.通过 attr_group_id 查找其"relation"体类
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id", attrgroupId);

        //1.1 得到 attr_group_id 查询的所有relation实体类
        List<AttrAttrgroupRelationEntity> entities = attrgroupRelationService.list(queryWrapper);

        //2.得到relation实体类对应的所有attr_id
        List<Long> attrIds = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        //2.1 如果查不到，返回null
        if(attrIds == null || attrIds.size() == 0){
            return null;
        }

        //3.根据attr_id查找所有属性实体类
        List<AttrEntity> attrEntities = baseMapper.selectBatchIds(attrIds);

        return attrEntities;
    }

    //12、删除属性与分组的关联关系
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //1.将AttrGroupRelationVo中的值，拷贝到 AttrAttrgroupRelationEntity
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            //2. 拷贝
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        //3.返回RelationEntity实体类，编写sql语句删除对应AttrAttrgroupRelationEntity的信息
        relationDao.deleteBatchRelation(entities);
    }

    //13、查询：获取"分组"没有关联的其他属性
    // attr_group_id -> catelog_id -> attr_group_ids -> attr_id -> attr_ids
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1.只能关联"当前分类"中的属性
        // attr_group_id -> catelog_id
        AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();

        //2.当前分组只能关联别的分组没有引用的属性
        //2.1 当前 分类(catelog_id) 下的 其他分组(attr_group) -> catelog_id相同的其他group
        QueryWrapper<AttrGroupEntity> groupWrapper = new QueryWrapper<>();
        groupWrapper.eq("catelog_id", catelogId);
        List<AttrGroupEntity> group = attrGroupService.list(groupWrapper);
        //得到这些group的id
        List<Long> gourpIds = group.stream().map(item -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());

        //2.2 分组(attr_group_id) -> 属性(attr_id)
        //in: 只要 attr_group_id 在 groupIds 里面，都能查到
        QueryWrapper<AttrAttrgroupRelationEntity> relationWrapper = new QueryWrapper<>();
        relationWrapper.in("attr_group_id", gourpIds);
        //找到这些attr_group_id 对应的 attr_id
        List<AttrAttrgroupRelationEntity> groupRelationEntities = attrgroupRelationService.list(relationWrapper);
        List<Long> attrIds = groupRelationEntities.stream().map(item -> {
            return item.getAttrId();
        }).collect(Collectors.toList());

        //2.3 从当前分类的所有属性中移除这些属性
        //2.3.1 attr表中：查找当前分类(catelog_id)中所有属性
        QueryWrapper<AttrEntity> attrWrapper = new QueryWrapper<>();
        attrWrapper.eq("catelog_id", catelogId);
        attrWrapper.eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        //2.3.2 从当前分类所有属性中，排除已经关联的属性
        if(attrIds != null && attrIds.size() > 0){
            attrWrapper.notIn("attr_id", attrIds);
        }

        //2.4 模糊查询等
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            attrWrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        //2.5 封装page
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), attrWrapper);
        PageUtils pageUtils = new PageUtils(page);

        return pageUtils;
    }

    //1.5.3 attrEntity -> search_type
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_id", attrIds);
        wrapper.eq("search_type", 0);
        List<AttrEntity> attrEntities = this.list(wrapper);
        //根据attrIds，找到search type = 0 的数据
        List<Long> attrIdsWithSearchZero = attrEntities.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());

        return attrIdsWithSearchZero;
    }
}


























