package com.hj.mall.product.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.result.Result;
import com.hj.mall.product.entity.Category;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.feign.CouponFeignClient;
import com.hj.mall.product.service.CategoryService;
import com.hj.mall.product.service.SkuService;
import com.hj.mall.product.vo.CategoryTreeVO;
import com.hj.mall.product.vo.HomeFloorConfigVO;
import com.hj.mall.product.vo.HomeIndexVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 首页控制器
 */
@Slf4j
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private static final String HOME_FLOOR_CONFIG_KEY = "mall:home:floor:config";
    private static final String HOME_INDEX_CACHE_KEY = "mall:home:index:guest";

    private final CategoryService categoryService;
    private final SkuService skuService;
    private final CouponFeignClient couponFeignClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final ThreadPoolExecutor homePageExecutor;

    /**
     * 获取首页数据
     */
    @GetMapping("/index")
    public Result<HomeIndexVO> index() {
        // 尝试从缓存获取
        String cached = stringRedisTemplate.opsForValue().get(HOME_INDEX_CACHE_KEY);
        if (StringUtils.hasText(cached)) {
            try {
                return Result.ok(JSON.parseObject(cached, HomeIndexVO.class));
            } catch (Exception e) {
                log.warn("[HomeController] 解析缓存数据失败: {}", e.getMessage());
            }
        }

        // 获取分类树
        List<CategoryTreeVO> categoryTree = categoryService.getCategoryTree();
        List<CategoryTreeVO> topCategories = categoryTree.stream()
                .limit(10)
                .collect(Collectors.toList());

        HomeIndexVO result = new HomeIndexVO();
        result.setTopCategories(convertToCategory(topCategories));

        // 并行加载各个模块
        CompletableFuture<Void> bannersFuture = safeRunAsync(() -> result.setBanners(buildBanners()), "首页广告加载失败");
        CompletableFuture<Void> subjectsFuture = safeRunAsync(() -> result.setSubjects(buildSubjects()), "首页专题加载失败");
        CompletableFuture<Void> flashSaleFuture = safeRunAsync(() -> result.setFlashSaleProducts(buildFlashSaleProducts()), "首页秒杀区加载失败");
        CompletableFuture<Void> recommendFuture = safeRunAsync(() -> result.setRecommendProducts(buildRecommendProducts()), "首页推荐加载失败");
        CompletableFuture<Void> floorsFuture = safeRunAsync(() -> result.setFloors(buildFloors(topCategories, categoryTree)), "首页楼层加载失败");

        CompletableFuture.allOf(bannersFuture, subjectsFuture, flashSaleFuture, recommendFuture, floorsFuture).join();

        // 缓存结果
        try {
            stringRedisTemplate.opsForValue().set(HOME_INDEX_CACHE_KEY, JSON.toJSONString(result), 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("[HomeController] 缓存首页数据失败: {}", e.getMessage());
        }

        return Result.ok(result);
    }

    /**
     * 获取楼层配置
     */
    @GetMapping("/floor-config")
    public Result<List<HomeFloorConfigVO>> floorConfig() {
        return Result.ok(getFloorConfigs());
    }

    /**
     * 保存楼层配置
     */
    @PostMapping("/floor-config")
    public Result<Void> saveFloorConfig(@RequestBody List<HomeFloorConfigVO> configs) {
        List<HomeFloorConfigVO> safeConfigs = configs != null ? configs : new ArrayList<>();
        try {
            stringRedisTemplate.opsForValue().set(HOME_FLOOR_CONFIG_KEY, JSON.toJSONString(safeConfigs));
            // 清除首页缓存
            stringRedisTemplate.delete(HOME_INDEX_CACHE_KEY);
        } catch (Exception e) {
            log.error("[HomeController] 保存楼层配置失败: {}", e.getMessage());
            return Result.error("保存楼层配置失败");
        }
        return Result.ok();
    }

    /**
     * 安全的异步执行
     */
    private CompletableFuture<Void> safeRunAsync(Runnable runnable, String errorMessage) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                log.warn("[HomeController] {}: {}", errorMessage, e.getMessage());
            }
        }, homePageExecutor);
    }

    /**
     * 构建推荐商品（简化版，返回最新商品）
     */
    private List<SkuInfo> buildRecommendProducts() {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getPublishStatus, 1)
                .orderByDesc(SkuInfo::getId)
                .last("LIMIT 12");
        return skuService.listPage(new Page<>(1, 12), null).getRecords();
    }

    /**
     * 构建秒杀商品（简化版，返回上架商品）
     */
    private List<SkuInfo> buildFlashSaleProducts() {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<SkuInfo>()
                .eq(SkuInfo::getPublishStatus, 1)
                .orderByDesc(SkuInfo::getId)
                .last("LIMIT 6");
        return skuService.listPage(new Page<>(1, 6), null).getRecords();
    }

    /**
     * 构建轮播图
     */
    private List<HomeIndexVO.BannerVO> buildBanners() {
        try {
            Result<List<Object>> response = couponFeignClient.getActiveAdvs();
            if (response != null && response.getCode() == 200 && response.getData() != null && !response.getData().isEmpty()) {
                return response.getData().stream().map(item -> {
                    JSONObject obj = (JSONObject) JSON.toJSON(item);
                    return buildBanner(
                            obj.getLong("id"),
                            obj.getString("name"),
                            obj.getString("pic"),
                            obj.getString("url")
                    );
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("[HomeController] 获取广告失败: {}", e.getMessage());
        }

        // 返回默认轮播图
        List<HomeIndexVO.BannerVO> banners = new ArrayList<>();
        banners.add(buildBanner(1L, "品质家电", "https://dummyimage.com/750x320/fd3f31/ffffff&text=Mall+Home+Appliance", ""));
        banners.add(buildBanner(2L, "数码热卖", "https://dummyimage.com/750x320/111111/ffffff&text=Mall+Digital", ""));
        banners.add(buildBanner(3L, "新人专享", "https://dummyimage.com/750x320/ff9f1c/ffffff&text=Mall+New+User", ""));
        return banners;
    }

    /**
     * 构建专题
     */
    private List<HomeIndexVO.SubjectVO> buildSubjects() {
        try {
            Result<List<Object>> response = couponFeignClient.getActiveSubjects();
            if (response != null && response.getCode() == 200 && response.getData() != null && !response.getData().isEmpty()) {
                return response.getData().stream().map(item -> {
                    JSONObject obj = (JSONObject) JSON.toJSON(item);
                    HomeIndexVO.SubjectVO subject = new HomeIndexVO.SubjectVO();
                    subject.setId(obj.getLong("id"));
                    subject.setName(obj.getString("name"));
                    subject.setTitle(obj.getString("title"));
                    subject.setSubTitle(obj.getString("subTitle"));
                    subject.setImg(obj.getString("img"));
                    subject.setUrl(obj.getString("url"));
                    return subject;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.warn("[HomeController] 获取专题失败: {}", e.getMessage());
        }

        // 返回默认专题
        List<HomeIndexVO.SubjectVO> subjects = new ArrayList<>();
        HomeIndexVO.SubjectVO digital = new HomeIndexVO.SubjectVO();
        digital.setId(1L);
        digital.setName("数码专题");
        digital.setTitle("数码会场");
        digital.setSubTitle("爆款直降 好物精选");
        digital.setImg("https://dummyimage.com/320x160/f3342f/ffffff&text=Digital");
        digital.setUrl("");
        subjects.add(digital);

        HomeIndexVO.SubjectVO appliance = new HomeIndexVO.SubjectVO();
        appliance.setId(2L);
        appliance.setName("家电专题");
        appliance.setTitle("家电焕新");
        appliance.setSubTitle("家居家电 超值好价");
        appliance.setImg("https://dummyimage.com/320x160/111111/ffffff&text=Appliance");
        appliance.setUrl("");
        subjects.add(appliance);
        return subjects;
    }

    /**
     * 构建轮播图对象
     */
    private HomeIndexVO.BannerVO buildBanner(Long id, String title, String pic, String url) {
        HomeIndexVO.BannerVO banner = new HomeIndexVO.BannerVO();
        banner.setId(id);
        banner.setTitle(title);
        banner.setPic(pic);
        banner.setUrl(url);
        return banner;
    }

    /**
     * 构建楼层
     */
    private List<HomeIndexVO.FloorVO> buildFloors(List<CategoryTreeVO> topCategories, List<CategoryTreeVO> categoryTree) {
        List<HomeFloorConfigVO> configs = getFloorConfigs();
        if (configs != null && !configs.isEmpty()) {
            List<HomeIndexVO.FloorVO> configuredFloors = configs.stream()
                    .map(config -> buildConfiguredFloor(config, categoryTree))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (!configuredFloors.isEmpty()) {
                return configuredFloors;
            }
        }

        // 返回默认楼层
        return topCategories.stream().limit(3).map(category -> {
            HomeIndexVO.FloorVO floor = new HomeIndexVO.FloorVO();
            floor.setCategoryId(category.getId());
            floor.setCategoryName(category.getName());
            List<Long> categoryIds = collectLeafCategoryIds(category);
            floor.setProducts(skuService.listPage(new Page<>(1, 6), null).getRecords().stream()
                    .filter(sku -> categoryIds.contains(sku.getSpuId()))
                    .limit(6)
                    .collect(Collectors.toList()));
            return floor;
        }).collect(Collectors.toList());
    }

    /**
     * 构建配置的楼层
     */
    private HomeIndexVO.FloorVO buildConfiguredFloor(HomeFloorConfigVO config, List<CategoryTreeVO> categoryTree) {
        if (config == null || config.getCategoryId() == null) {
            return null;
        }
        CategoryTreeVO category = findCategoryById(categoryTree, config.getCategoryId());
        if (category == null) {
            return null;
        }

        HomeIndexVO.FloorVO floor = new HomeIndexVO.FloorVO();
        floor.setCategoryId(category.getId());
        floor.setCategoryName(StringUtils.hasText(config.getFloorName()) ? config.getFloorName() : category.getName());

        int productLimit = config.getProductLimit() == null || config.getProductLimit() <= 0 ? 6 : config.getProductLimit();
        List<SkuInfo> products;

        if (config.getSkuIds() != null && !config.getSkuIds().isEmpty()) {
            // 获取指定SKU
            List<SkuInfo> allSkus = skuService.listPage(new Page<>(1, 100), null).getRecords();
            Map<Long, SkuInfo> skuMap = allSkus.stream()
                    .collect(Collectors.toMap(SkuInfo::getId, sku -> sku));
            products = config.getSkuIds().stream()
                    .map(skuMap::get)
                    .filter(Objects::nonNull)
                    .limit(productLimit)
                    .collect(Collectors.toList());
        } else {
            // 获取分类下的商品
            List<Long> categoryIds = collectLeafCategoryIds(category);
            products = skuService.listPage(new Page<>(1, productLimit), null).getRecords().stream()
                    .filter(sku -> categoryIds.contains(sku.getSpuId()))
                    .limit(productLimit)
                    .collect(Collectors.toList());
        }

        floor.setProducts(products);
        return floor;
    }

    /**
     * 获取楼层配置
     */
    private List<HomeFloorConfigVO> getFloorConfigs() {
        try {
            String content = stringRedisTemplate.opsForValue().get(HOME_FLOOR_CONFIG_KEY);
            if (!StringUtils.hasText(content)) {
                return Collections.emptyList();
            }
            List<HomeFloorConfigVO> configs = JSON.parseArray(content, HomeFloorConfigVO.class);
            return configs != null ? configs : Collections.emptyList();
        } catch (Exception e) {
            log.warn("[HomeController] 获取楼层配置失败: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 根据ID查找分类
     */
    private CategoryTreeVO findCategoryById(List<CategoryTreeVO> categories, Long categoryId) {
        if (categories == null || categories.isEmpty() || categoryId == null) {
            return null;
        }
        for (CategoryTreeVO category : categories) {
            if (categoryId.equals(category.getId())) {
                return category;
            }
            CategoryTreeVO childMatch = findCategoryById(category.getChildren(), categoryId);
            if (childMatch != null) {
                return childMatch;
            }
        }
        return null;
    }

    /**
     * 收集叶子分类ID
     */
    private List<Long> collectLeafCategoryIds(CategoryTreeVO category) {
        List<Long> result = new ArrayList<>();
        if (category.getChildren() == null || category.getChildren().isEmpty()) {
            result.add(category.getId());
            return result;
        }
        for (CategoryTreeVO child : category.getChildren()) {
            result.addAll(collectLeafCategoryIds(child));
        }
        return result;
    }

    /**
     * 转换为Category对象
     */
    private List<Category> convertToCategory(List<CategoryTreeVO> categoryTreeVOs) {
        if (categoryTreeVOs == null || categoryTreeVOs.isEmpty()) {
            return Collections.emptyList();
        }
        return categoryTreeVOs.stream().map(vo -> {
            Category category = new Category();
            category.setId(vo.getId());
            category.setName(vo.getName());
            category.setParentId(vo.getParentId());
            category.setLevel(vo.getLevel());
            category.setIcon(vo.getIcon());
            category.setSort(vo.getSort());
            return category;
        }).collect(Collectors.toList());
    }
}
