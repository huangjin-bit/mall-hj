package com.hj.mall.ware.service;

import com.hj.mall.ware.entity.PurchaseDetail;

import java.util.List;

public interface PurchaseDetailService {

    List<PurchaseDetail> listByPurchaseId(Long purchaseId);

    void saveBatch(List<PurchaseDetail> details);
}
