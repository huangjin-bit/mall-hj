package com.hj.mall.product.service.impl;

import com.hj.mall.product.entity.SpuInfoDesc;
import com.hj.mall.product.mapper.SpuInfoDescMapper;
import com.hj.mall.product.service.SpuInfoDescService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpuInfoDescServiceImpl implements SpuInfoDescService {

    private final SpuInfoDescMapper spuInfoDescMapper;

    @Override
    public SpuInfoDesc getById(Long spuId) {
        return spuInfoDescMapper.selectById(spuId);
    }

    @Override
    public void save(SpuInfoDesc desc) {
        spuInfoDescMapper.insert(desc);
    }

    @Override
    public void updateById(SpuInfoDesc desc) {
        spuInfoDescMapper.updateById(desc);
    }
}
