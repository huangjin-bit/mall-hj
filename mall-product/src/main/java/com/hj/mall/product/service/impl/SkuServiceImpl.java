package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.product.entity.SkuImages;
import com.hj.mall.product.entity.SkuInfo;
import com.hj.mall.product.entity.SkuSaleAttrValue;
import com.hj.mall.product.mapper.SkuImagesMapper;
import com.hj.mall.product.mapper.SkuInfoMapper;
import com.hj.mall.product.mapper.SkuSaleAttrValueMapper;
import com.hj.mall.product.service.SkuService;
import com.hj.mall.product.vo.SkuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkuServiceImpl implements SkuService {

    private final SkuInfoMapper skuInfoMapper;
    private final SkuImagesMapper skuImagesMapper;
    private final SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public SkuVO getSkuDetail(Long skuId) {
        SkuInfo sku = skuInfoMapper.selectById(skuId);
        if (sku == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }

        SkuVO vo = new SkuVO();
        vo.setId(sku.getId());
        vo.setSpuId(sku.getSpuId());
        vo.setSkuName(sku.getSkuName());
        vo.setSkuDesc(sku.getSkuDesc());
        vo.setPrice(sku.getPrice());
        vo.setOriginalPrice(sku.getOriginalPrice());
        vo.setSkuImg(sku.getSkuImg());
        vo.setWeight(sku.getWeight());
        vo.setPublishStatus(sku.getPublishStatus());

        // SKU 图片
        List<SkuImages> images = skuImagesMapper.selectList(
                new LambdaQueryWrapper<SkuImages>()
                        .eq(SkuImages::getSkuId, skuId)
                        .orderByAsc(SkuImages::getSort)
        );
        vo.setImages(images.stream().map(SkuImages::getImgUrl).toList());

        // 销售属性
        List<SkuSaleAttrValue> saleAttrs = skuSaleAttrValueMapper.selectList(
                new LambdaQueryWrapper<SkuSaleAttrValue>()
                        .eq(SkuSaleAttrValue::getSkuId, skuId)
                        .orderByAsc(SkuSaleAttrValue::getSort)
        );
        vo.setSaleAttrs(saleAttrs.stream().map(sa -> {
            SkuVO.SaleAttrItem item = new SkuVO.SaleAttrItem();
            item.setAttrId(sa.getAttrId());
            item.setAttrName(sa.getAttrName());
            item.setAttrValue(sa.getAttrValue());
            return item;
        }).toList());

        return vo;
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {
        SkuInfo sku = skuInfoMapper.selectById(skuId);
        if (sku == null) {
            throw new BizException(ResultCode.PRODUCT_NOT_FOUND);
        }
        return sku;
    }

    @Override
    public IPage<SkuInfo> listPage(Page<SkuInfo> page, Long spuId) {
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        if (spuId != null) {
            wrapper.eq(SkuInfo::getSpuId, spuId);
        }
        wrapper.orderByAsc(SkuInfo::getSort);
        return skuInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(SkuInfo sku) {
        skuInfoMapper.insert(sku);
    }

    @Override
    public void updateById(SkuInfo sku) {
        skuInfoMapper.updateById(sku);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        skuInfoMapper.deleteBatchIds(ids);
    }
}
