package com.hj.mall.search.service;

import com.hj.mall.search.dto.SearchParam;
import com.hj.mall.search.model.SkuEsModel;
import com.hj.mall.search.vo.SearchResult;

import java.util.List;

public interface SearchService {
    SearchResult search(SearchParam param);
    void saveOrUpdate(SkuEsModel model);
    void saveBatch(List<SkuEsModel> models);
    void deleteBySkuId(Long skuId);
}
