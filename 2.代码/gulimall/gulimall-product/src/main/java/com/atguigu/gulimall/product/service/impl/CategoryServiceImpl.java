package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catalog3Vo;
import com.atguigu.gulimall.product.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
    /**
     * 级联更新所有数据			[分区名默认是就是缓存的前缀] SpringCache: 不加锁
     *
     * @CacheEvict: 缓存失效模式		--- 页面一修改 然后就清除这两个缓存
     * key = "'getLevel1Categorys'" : 记得加单引号 [子解析字符串]
     *
     * @Caching: 同时进行多种缓存操作
     *
     * @CacheEvict(value = {"category"}, allEntries = true) : 删除这个分区所有数据
     *
     * @CachePut: 这次查询操作写入缓存
     */
    @CacheEvict(value = {"category"}, allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        //1.更新当前"分类表"中的数据'
        baseMapper.updateById(category);
        //2.更新其他存有"分类名的表"
        //2.1 更新 分类关联 表
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    //1. 查出所有1级分类
    /**
     * @Cacheable: 当前方法的结果需要缓存 并指定缓存名字
     *  缓存的value值 默认使用jdk序列化
     *  默认ttl时间 -1
     *	key: 里面默认会解析表达式 字符串用 ''
     *
     *  自定义:
     *  	1.指定生成缓存使用的key
     *  	2.指定缓存数据存活时间	[配置文件中修改]
     *  	3.将数据保存为json格式
     *
     *  sync = true: --- 开启同步锁
     *
     */
    @Cacheable(value={"category"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("查找一级分类");
        //1。一级分类 = parent_cid = 0
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_cid", 0);

        List<CategoryEntity> categoryEntities = baseMapper.selectList(wrapper);
        return categoryEntities;
    }


    //第一次查询的所有 CategoryEntity 然后根据 parent_cid去这里找
    private List<CategoryEntity> getCategoryEntities(List<CategoryEntity> entityList, Long parent_cid) {

        return entityList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
    }

    //web 2.得到二级、三级子分类
    @Cacheable(value={"category"}, key = "#root.methodName", sync = true)
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        List<CategoryEntity> entityList = baseMapper.selectList(null);
        // 查询所有一级分类
        List<CategoryEntity> level1 = getCategoryEntities(entityList, 0L);
        Map<String, List<Catelog2Vo>> parent_cid = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 拿到每一个一级分类 然后查询他们的二级分类
            List<CategoryEntity> entities = getCategoryEntities(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                    // 找当前二级分类的三级分类
                    List<CategoryEntity> level3 = getCategoryEntities(entityList, l2.getCatId());
                    // 三级分类有数据的情况下
                    if (level3 != null) {
                        List<Catalog3Vo> catalog3Vos = level3.stream().map(l3 -> new Catalog3Vo(l3.getCatId().toString(), l3.getName(), l2.getCatId().toString())).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    /**
     * 缓存中存的所有字符串都是JSON
     * TODO 可能会产生堆外内存溢出
     */
    public Map<String, List<Catelog2Vo>> getCatelogJson2() {

        /**
         * 1.空结果缓存 解决缓存穿透
         * 2.设置过期时间 解决缓存雪崩
         * 3.加锁 解决缓存击穿
         */
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Map<String, List<Catelog2Vo>> catelogJson;
        // 缓存中没有数据
        String catelogJSON = operations.get("catelogJSON");
        if (StringUtils.isEmpty(catelogJSON)) {
            catelogJson = getCatelogJsonFromDBWithRedisLock();
        } else {
            catelogJson = JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        return catelogJson;
    }



    //TODO redis 优化分类
    /**
     * redis无缓存 查询数据库
     */
    private Map<String, List<Catelog2Vo>> getDataFromDB() {
        String catelogJSON = stringRedisTemplate.opsForValue().get("catelogJSON");
        // 如果redis中有数据，则直接返回
        if (!StringUtils.isEmpty(catelogJSON)) {
            return JSON.parseObject(catelogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }

        // 优化：将查询变为一次
        List<CategoryEntity> entityList = baseMapper.selectList(null);

        // 查询所有一级分类
        List<CategoryEntity> level1 = getCategoryEntities(entityList, 0L);
        Map<String, List<Catelog2Vo>> parent_cid = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 拿到每一个一级分类 然后查询他们的二级分类
            List<CategoryEntity> entities = getCategoryEntities(entityList, v.getCatId());
            List<Catelog2Vo> catelog2Vos = null;
            if (entities != null) {
                catelog2Vos = entities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), l2.getName(), l2.getCatId().toString(), null);
                    // 找当前二级分类的三级分类
                    List<CategoryEntity> level3 = getCategoryEntities(entityList, l2.getCatId());
                    // 三级分类有数据的情况下
                    if (level3 != null) {
                        List<Catalog3Vo> catalog3Vos = level3.stream().map(l3 -> new Catalog3Vo(l3.getCatId().toString(), l3.getName(), l2.getCatId().toString())).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catalog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        // 优化：查询到数据库就再锁还没结束之前放入缓存
        stringRedisTemplate.opsForValue().set("catelogJSON", JSON.toJSONString(parent_cid), 1, TimeUnit.DAYS);
        return parent_cid;
    }

    /**
     * redis没有数据 查询DB [本地锁解决方案]
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDBWithLocalLock() {
        synchronized (this) {
            // 双重检查 是否有缓存
            return getDataFromDB();
        }
    }

    /**
     * 分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDBWithRedisLock() {
        // 1.占分布式锁  设置这个锁10秒自动删除 [原子操作]
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);

        if (lock) {
            // 2.设置过期时间加锁成功 获取数据释放锁 [分布式下必须是Lua脚本删锁,不然会因为业务处理时间、网络延迟等等引起数据还没返回锁过期或者返回的过程中过期 然后把别人的锁删了]
            Map<String, List<Catelog2Vo>> data;
            try {
                data = getDataFromDB();
            } finally {
			// stringRedisTemplate.delete("lock");
                String lockValue = stringRedisTemplate.opsForValue().get("lock");

                // 删除也必须是原子操作 Lua脚本操作 删除成功返回1 否则返回0
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                // 原子删锁
                stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return data;
        } else {
            // 重试加锁

            try {
                // 登上两百毫秒
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //自旋
            return getCatelogJsonFromDBWithRedisLock();
        }
    }

}







