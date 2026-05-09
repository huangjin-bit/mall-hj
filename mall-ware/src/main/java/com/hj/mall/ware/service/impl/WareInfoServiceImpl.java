package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.ware.entity.WareInfo;
import com.hj.mall.ware.mapper.WareInfoMapper;
import com.hj.mall.ware.service.WareInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WareInfoServiceImpl implements WareInfoService {

    private final WareInfoMapper wareInfoMapper;

    @Override
    public WareInfo getById(Long id) {
        WareInfo wareInfo = wareInfoMapper.selectById(id);
        if (wareInfo == null) {
            throw new BizException(ResultCode.WARE_NOT_FOUND);
        }
        return wareInfo;
    }

    @Override
    public IPage<WareInfo> listPage(Page<WareInfo> page, String key) {
        LambdaQueryWrapper<WareInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(key)) {
            wrapper.like(WareInfo::getName, key)
                    .or().like(WareInfo::getAddress, key)
                    .or().like(WareInfo::getPhone, key);
        }
        wrapper.orderByDesc(WareInfo::getCreateTime);
        return wareInfoMapper.selectPage(page, wrapper);
    }

    @Override
    public void save(WareInfo wareInfo) {
        wareInfoMapper.insert(wareInfo);
    }

    @Override
    public void updateById(WareInfo wareInfo) {
        wareInfoMapper.updateById(wareInfo);
    }

    @Override
    public void removeBatch(List<Long> ids) {
        wareInfoMapper.deleteBatchIds(ids);
    }
}
