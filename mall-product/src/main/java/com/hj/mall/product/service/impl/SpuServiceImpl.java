package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.product.entity.SkuImages;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SkuSaleAttrValue;
import com.hj.mall.product.entity.SpuAttrValue;
import com.hj.mall.product.entity.SpuImages;
import com.hj.mall.product.entity.SpuInfo;
import com.hj.mall.product.entity.SpuInfoDesc;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.Category;
import com.hj.mall.product.mapper.SkuImagesMapper;
import com.hj.mall.product.mapper.SkuInfoMapper;
import com.hj.mall.product.mapper.SkuSaleAttrValueMapper;
import com.hj.mall.product.mapper.SpuAttrValueMapper;
import com.hj.mall.product.mapper.SpuImagesMapper;
import com.hj.mall.product.mapper.SpuInfoDescMapper;
import com.hj.mall.product.mapper.SpuInfoMapper;
import com.hj.mall.product.mapper.BrandMapper;
import com.hj.mall.product.mapper.CategoryMapper;
import com.hj.mall.product.service.SpuService;
import com.hj.mall.product.vo.SpuDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpuServiceImpl implements SpuService {

    private final SpuInfoMapper spuInfoMapper;
    private final SpuInfoDescMapper spuInfoDescMapper;
    private final SpuImagesMapper spuImagesMapper;
    private final SpuAttrValueMapper spuAttrValueMapper;
    private final SkuInfoMapper skuInfoMapper;
    private final SkuImagesMapper skuImagesMapper;
    private final SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    private final BrandMapper brandMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public SpuDetailVO getSpuDetail(Long spuId) {
        SpuInfo spu = spuInfoMapper.selectById(spuId);
        if (spu == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }

        SpuDetailVO vo = new SpuDetailVO();
        vo.setId(spu.getId());
        vo.setSpuName(spu.getSpuName());
        vo.setSpuDescription(spu.getSpuDescription());
        vo.setCategoryId(spu.getCategoryId());
        vo.setBrandId(spu.getBrandId());
        vo.setWeight(spu.getWeight());
        vo.setPublishStatus(spu.getPublishStatus());
        vo.setNewStatus(spu.getNewStatus());
        vo.setRecommendStatus(spu.getRecommendStatus());

        // 分类名
        Category category = categoryMapper.selectById(spu.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        // 品牌名 + logo
        if (spu.getBrandId() != null) {
            Brand brand = brandMapper.selectById(spu.getBrandId());
            if (brand != null) {
                vo.setBrandName(brand.getName());
                vo.setBrandLogo(brand.getLogo());
            }
        }

        // 富文本描述
        SpuInfoDesc desc = spuInfoDescMapper.selectById(spuId);
        if (desc != null) {
            vo.setDescription(desc.getDescription());
        }

        // SPU 图片
        List<SpuImages> spuImages = spuImagesMapper.selectList(
                new LambdaQueryWrapper<SpuImages>()
                        .eq(SpuImages::getSpuId, spuId)
                        .orderByAsc(SpuImages::getSort)
        );
        vo.setImages(spuImages.stream().map(SpuImages::getImgUrl).toList());

        // 基本属性
        List<SpuAttrValue> attrValues = spuAttrValueMapper.selectList(
                new LambdaQueryWrapper<SpuAttrValue>()
                        .eq(SpuAttrValue::getSpuId, spuId)
                        .orderByAsc(SpuAttrValue::getSort)
        );
        vo.setBaseAttrs(attrValues.stream().map(a -> {
            SpuDetailVO.AttrItem item = new SpuDetailVO.AttrItem();
            item.setAttrId(a.getAttrId());
            item.setAttrName(a.getAttrName());
            item.setAttrValue(a.getAttrValue());
            return item;
        }).toList());

        // SKU 列表
        List<SkuInfo> skuList = skuInfoMapper.selectList(
                new LambdaQueryWrapper<SkuInfo>()
                        .eq(SkuInfo::getSpuId, spuId)
                        .orderByAsc(SkuInfo::getSort)
        );
        vo.setSkus(skuList.stream().map(sku -> {
            SpuDetailVO.SkuItem skuItem = new SpuDetailVO.SkuItem();
            skuItem.setSkuId(sku.getId());
            skuItem.setSkuName(sku.getSkuName());
            skuItem.setPrice(sku.getPrice());
            skuItem.setOriginalPrice(sku.getOriginalPrice());
            skuItem.setSkuImg(sku.getSkuImg());

            // SKU 销售属性
            List<SkuSaleAttrValue> saleAttrs = skuSaleAttrValueMapper.selectList(
                    new LambdaQueryWrapper<SkuSaleAttrValue>()
                            .eq(SkuSaleAttrValue::getSkuId, sku.getId())
                            .orderByAsc(SkuSaleAttrValue::getSort)
            );
            skuItem.setSaleAttrs(saleAttrs.stream().map(sa -> {
                SpuDetailVO.SaleAttrItem sai = new SpuDetailVO.SaleAttrItem();
                sai.setAttrId(sa.getAttrId());
                sai.setAttrName(sa.getAttrName());
                sai.setAttrValue(sa.getAttrValue());
                return sai;
            }).toList());

            // SKU 图片
            List<SkuImages> skuImgs = skuImagesMapper.selectList(
                    new LambdaQueryWrapper<SkuImages>()
                            .eq(SkuImages::getSkuId, sku.getId())
                            .orderByAsc(SkuImages::getSort)
            );
            skuItem.setImages(skuImgs.stream().map(SkuImages::getImgUrl).toList());

            return skuItem;
        }).toList());

        return vo;
    }

    @Override
    public SpuInfo getSpuInfo(Long spuId) {
        SpuInfo spu = spuInfoMapper.selectById(spuId);
        if (spu == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }
        return spu;
    }

    @Override
    public IPage<SpuInfo> listPage(Page<SpuInfo> page, String key, Integer status) {
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(key)) {
            wrapper.like(SpuInfo::getSpuName, key);
        }
        if (status != null) {
            wrapper.eq(SpuInfo::getPublishStatus, status);
        }
        wrapper.orderByDesc(SpuInfo::getCreateTime);
        return spuInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(SpuInfo spu) {
        spuInfoMapper.insert(spu);
    }

    @Override
    public void updateById(SpuInfo spu) {
        spuInfoMapper.updateById(spu);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        spuInfoMapper.deleteBatchIds(ids);
    }

    @Override
    public void publish(Long spuId, Integer status) {
        SpuInfo spu = spuInfoMapper.selectById(spuId);
        if (spu == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }
        spu.setPublishStatus(status);
        spuInfoMapper.updateById(spu);
    }
}
