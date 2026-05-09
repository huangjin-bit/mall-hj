package com.hj.mall.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.seckill.entity.SeckillSession;

import java.util.List;

/**
 * 秒杀场次服务接口
 */
public interface SeckillSessionService {

    /**
     * 分页查询秒杀场次
     * @param page 分页参数
     * @param name 场次名称（可选）
     * @return 分页结果
     */
    IPage<SeckillSession> listPage(Page<SeckillSession> page, String name);

    /**
     * 查询当前启用的秒杀场次列表
     * @return 场次列表
     */
    List<SeckillSession> listActiveSessions();

    /**
     * 根据ID查询场次
     * @param id 场次ID
     * @return 场次信息
     */
    SeckillSession getById(Long id);

    /**
     * 保存场次
     * @param session 场次信息
     */
    void save(SeckillSession session);

    /**
     * 更新场次
     * @param session 场次信息
     */
    void updateById(SeckillSession session);

    /**
     * 批量删除场次
     * @param ids 场次ID列表
     */
    void removeBatch(List<Long> ids);
}
