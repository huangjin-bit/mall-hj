package com.hj.mall.search.repository;

import com.hj.mall.search.model.SkuEsModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuEsRepository extends ElasticsearchRepository<SkuEsModel, Long> {
}
