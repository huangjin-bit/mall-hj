package com.hj.mall.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.seckill.entity.HomeSubject;
import com.hj.mall.seckill.mapper.HomeSubjectMapper;
import com.hj.mall.seckill.service.HomeSubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页专题服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeSubjectServiceImpl implements HomeSubjectService {

    private final HomeSubjectMapper homeSubjectMapper;

    @Override
    public IPage<HomeSubject> listPage(Page<HomeSubject> page, String name) {
        log.info("[HomeSubjectService] 分页查询专题, page={}, name={}", page.getCurrent(), name);

        LambdaQueryWrapper<HomeSubject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(HomeSubject::getName, name);
        }
        wrapper.orderByDesc(HomeSubject::getCreateTime);

        return homeSubjectMapper.selectPage(page, wrapper);
    }

    @Override
    public List<HomeSubject> listActiveSubjects() {
        log.info("[HomeSubjectService] 查询当前启用的专题");

        LambdaQueryWrapper<HomeSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomeSubject::getStatus, 1)
               .orderByAsc(HomeSubject::getSort);

        return homeSubjectMapper.selectList(wrapper);
    }

    @Override
    public HomeSubject getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        HomeSubject subject = homeSubjectMapper.selectById(id);
        if (subject == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        return subject;
    }

    @Override
    public void save(HomeSubject subject) {
        log.info("[HomeSubjectService] 保存专题, name={}", subject.getName());

        // 参数校验
        if (StringUtils.isBlank(subject.getName())) {
            throw new BizException(400, "专题名称不能为空");
        }

        homeSubjectMapper.insert(subject);
        log.info("[HomeSubjectService] 专题保存成功, id={}", subject.getId());
    }

    @Override
    public void updateById(HomeSubject subject) {
        log.info("[HomeSubjectService] 更新专题, id={}", subject.getId());

        HomeSubject existSubject = getById(subject.getId());
        if (existSubject == null) {
            throw new BizException(ResultCode.NOT_FOUND);
        }

        homeSubjectMapper.updateById(subject);
        log.info("[HomeSubjectService] 专题更新成功, id={}", subject.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[HomeSubjectService] 批量删除专题, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        homeSubjectMapper.deleteBatchIds(ids);
        log.info("[HomeSubjectService] 批量删除成功, count={}", ids.size());
    }
}
