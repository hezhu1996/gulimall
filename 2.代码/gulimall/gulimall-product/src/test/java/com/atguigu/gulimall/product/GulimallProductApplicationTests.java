package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.component.SmsComponent;
import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.SkuSaleAttrValueDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;

import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.ItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallProductApplicationTests {

	@Autowired
	BrandService brandService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	AttrGroupDao attrGroupDao;

	@Autowired
	SkuSaleAttrValueDao skuSaleAttrValueDao;

	@Autowired
	SmsComponent smsComponent;

	@Test
	public void sendCode() {
		smsComponent.sendSmsCode("18600146610", "888888");
		System.out.println("============== 成功 ================");
	}

	@Test
	public void test() {
		// List<SpuItemAttrGroup> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(1L, 225L);
		// System.out.println(attrGroupWithAttrsBySpuId);

		List<ItemSaleAttrVo> saleAttrsBuSpuId = skuSaleAttrValueDao.getSaleAttrsBuSpuId((long) 1);
		System.out.println(saleAttrsBuSpuId);
	}

	@Test
	public void testStringRedisTemplate() {
		//hello   world
		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		//保存
		ops.set("hello", "world_"+ UUID.randomUUID().toString());

		//查询
		String hello = ops.get("hello");
		System.out.println(hello);
	}

	@Test
	public void contextLoads() {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setBrandId(1L);
		brandEntity.setDescript("xiaomi");
		brandService.updateById(brandEntity);
		System.out.println("=========保存成功---------------");
	}

	@Test
	public void testFindPath() {
		Long[] catelogPath = categoryService.findCatelogPath(225L);
		log.info("Path:{}", Arrays.asList(catelogPath));

	}

}
