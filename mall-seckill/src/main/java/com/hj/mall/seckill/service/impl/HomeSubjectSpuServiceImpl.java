package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.HomeSubjectSpu;
import com.hj.mall.seckill.mapper.HomeSubjectSpuMapper;
import com.hj.mall.seckill.service.HomeSubjectSpuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页专题商品关联服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeSubjectSpuServiceImpl implements HomeSubjectSpuService {

    private final HomeSubjectSpuMapper homeSubjectSpuMapper;

    @Override
    public IPage<HomeSubjectSpu> listPage(Page<HomeSubjectSpu> page) {
        log.info("[HomeSubjectSpuService] 分页查询首页专题商品关联, page={}", page.getCurrent());

        LambdaQueryWrapper<HomeSubjectSpu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(HomeSubjectSpu::getSort)
               .orderByDesc(HomeSubjectSpu::getCreateTime);

        return homeSubjectSpuMapper.selectPage(page, wrapper);
    }

    @Override
    public HomeSubjectSpu getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        HomeSubjectSpu entity = homeSubjectSpuMapper.selectById(id);
        if (entity == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return entity;
    }

    @Override
    public void save(HomeSubjectSpu entity) {
        log.info("[HomeSubjectSpuService] 保存首页专题商品关联, subjectId={}, spuId={}",
                entity.getSubjectId(), entity.getSpuId());

        // 参数校验
        if (entity.getSubjectId() == null) {
            throw new BizException(400, "专题ID不能为空");
        }
        if (entity.getSpuId() == null) {
            throw new BizException(400, "商品SPU ID不能为空");
        }
        if (entity.getSort() == null) {
            entity.setSort(0);
        }

        homeSubjectSpuMapper.insert(entity);
        log.info("[HomeSubjectSpuService] 首页专题商品关联保存成功, id={}", entity.getId());
    }

    @Override
    public void updateById(HomeSubjectSpu entity) {
        log.info("[HomeSubjectSpuService] 更新首页专题商品关联, id={}", entity.getId());

        if (entity.getId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        homeSubjectSpuMapper.updateById(entity);
        log.info("[HomeSubjectSpuService] 首页专题商品关联更新成功, id={}", entity.getId());
    }

    @Override
    public void removeBatch(List<Long> ids) {
        log.info("[HomeSubjectSpuService] 批量删除首页专题商品关联, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        homeSubjectSpuMapper.deleteBatchIds(ids);
        log.info("[HomeSubjectSpuService] 批量删除成功, count={}", ids.size());
    }
}
