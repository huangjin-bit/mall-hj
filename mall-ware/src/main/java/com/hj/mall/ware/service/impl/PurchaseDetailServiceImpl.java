package com.hj.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.ware.entity.PurchaseDetail;
import com.hj.mall.ware.mapper.PurchaseDetailMapper;
import com.hj.mall.ware.service.PurchaseDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseDetailServiceImpl implements PurchaseDetailService {

    private final PurchaseDetailMapper purchaseDetailMapper;

    @Override
    public List<PurchaseDetail> listByPurchaseId(Long purchaseId) {
        return purchaseDetailMapper.selectList(
                new LambdaQueryWrapper<PurchaseDetail>()
                        .eq(PurchaseDetail::getPurchaseId, purchaseId)
                        .orderByAsc(PurchaseDetail::getCreateTime)
        );
    }

    @Override
    public void saveBatch(List<PurchaseDetail> details) {
        if (details != null && !details.isEmpty()) {
            details.forEach(purchaseDetailMapper::insert);
        }
    }
}
