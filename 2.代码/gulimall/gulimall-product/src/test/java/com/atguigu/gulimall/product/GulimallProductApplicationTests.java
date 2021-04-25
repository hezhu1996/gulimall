package com.atguigu.gulimall.product;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;

import com.atguigu.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
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
