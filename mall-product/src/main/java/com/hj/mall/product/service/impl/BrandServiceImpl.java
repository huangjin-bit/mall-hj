package com.hj.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.Brand;
import com.hj.mall.product.entity.CategoryBrandRelation;
import com.hj.mall.product.mapper.BrandMapper;
import com.hj.mall.product.mapper.CategoryBrandRelationMapper;
import com.hj.mall.product.service.BrandService;
import com.hj.mall.product.vo.BrandVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandMapper brandMapper;
    private final CategoryBrandRelationMapper relationMapper;

    @Override
    public List<BrandVO> listBrands(Long categoryId) {
        if (categoryId != null) {
            List<CategoryBrandRelation> relations = relationMapper.selectList(
                    new LambdaQueryWrapper<CategoryBrandRelation>()
                            .eq(CategoryBrandRelation::getCategoryId, categoryId)
                            .orderByAsc(CategoryBrandRelation::getSort)
            );
            return relations.stream()
                    .map(r -> {
                        Brand brand = brandMapper.selectById(r.getBrandId());
                        if (brand == null || brand.getStatus() != 1) {
                            return null;
                        }
                        return toVO(brand);
                    })
                    .filter(Objects::nonNull)
                    .toList();
        }

        List<Brand> brands = brandMapper.selectList(
                new LambdaQueryWrapper<Brand>()
                        .eq(Brand::getStatus, 1)
                        .orderByAsc(Brand::getSort)
        );
        return brands.stream().map(this::toVO).toList();
    }

    private BrandVO toVO(Brand brand) {
        BrandVO vo = new BrandVO();
        vo.setId(brand.getId());
        vo.setName(brand.getName());
        vo.setLogo(brand.getLogo());
        return vo;
    }

    @Override
    public IPage<Brand> listPage(Page<Brand> page, String key) {
        LambdaQueryWrapper<Brand> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(key)) {
            wrapper.like(Brand::getName, key);
        }
        wrapper.orderByAsc(Brand::getSort);
        return brandMapper.selectPage(page, wrapper);
    }

    @Override
    public Brand getById(Long id) {
        return brandMapper.selectById(id);
    }

    @Override
    public void save(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void updateById(Brand brand) {
        brandMapper.updateById(brand);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        brandMapper.deleteBatchIds(ids);
    }
}
