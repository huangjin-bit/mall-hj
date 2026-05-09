package com.hj.mall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.product.entity.SpuAuditLog;

public interface SpuAuditLogService {

    IPage<SpuAuditLog> listPage(Page<SpuAuditLog> page, Long spuId);
}
