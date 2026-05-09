package com.hj.mall.search.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.hj.mall.search.dto.SearchParam;
import com.hj.mall.search.model.SkuEsModel;
import com.hj.mall.search.repository.SkuEsRepository;
import com.hj.mall.search.service.SearchService;
import com.hj.mall.search.vo.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SkuEsRepository skuEsRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public SearchResult search(SearchParam param) {
        log.info("[搜索服务] keyword={}, categoryId={}, brandId={}, page={}, size={}, sort={}",
                param.getKeyword(), param.getCategoryId(), param.getBrandId(),
                param.getPage(), param.getSize(), param.getSort());

        NativeQueryBuilder queryBuilder = NativeQuery.builder();

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        if (param.getKeyword() != null && !param.getKeyword().isEmpty()) {
            boolQueryBuilder.must(m -> m
                    .multiMatch(multi -> multi
                            .query(param.getKeyword())
                            .fields("skuName", "spuName", "spuDescription")
                            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BestFields)
                    )
            );
        }

        if (param.getCategoryId() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field("categoryId")
                            .value(param.getCategoryId())
                    )
            );
        }

        if (param.getBrandId() != null) {
            boolQueryBuilder.filter(f -> f
                    .term(t -> t
                            .field("brandId")
                            .value(param.getBrandId())
                    )
            );
        }

        if (param.getMinPrice() != null || param.getMaxPrice() != null) {
            var rangeBuilder = new co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery.Builder()
                    .field("price");

            if (param.getMinPrice() != null) {
                rangeBuilder.gte(JsonData.of(param.getMinPrice().doubleValue()));
            }
            if (param.getMaxPrice() != null) {
                rangeBuilder.lte(JsonData.of(param.getMaxPrice().doubleValue()));
            }

            boolQueryBuilder.filter(f -> f.range(rangeBuilder.build()));
        }

        queryBuilder.withQuery(boolQueryBuilder.build()._toQuery());

        Sort sort = buildSort(param.getSort());
        Pageable pageable = PageRequest.of(param.getPage() - 1, param.getSize(), sort);
        queryBuilder.withPageable(pageable);

        NativeQuery query = queryBuilder.build();

        SearchHits<SkuEsModel> searchHits = elasticsearchOperations.search(query, SkuEsModel.class);

        List<SkuEsModel> products = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        SearchResult result = new SearchResult();
        result.setProducts(products);
        result.setTotal(searchHits.getTotalHits());
        result.setPageNum(param.getPage());
        result.setPageSize(param.getSize());
        result.setTotalPages((int) Math.ceil((double) searchHits.getTotalHits() / param.getSize()));

        log.info("[搜索服务] 搜索完成，结果数: {}", result.getTotal());
        return result;
    }

    private Sort buildSort(String sortType) {
        if (sortType == null || sortType.isEmpty()) {
            return Sort.unsorted();
        }

        return switch (sortType) {
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "price");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
            case "new" -> Sort.by(Sort.Direction.DESC, "createTime");
            default -> Sort.unsorted();
        };
    }

    @Override
    public void saveOrUpdate(SkuEsModel model) {
        log.info("[搜索服务] 保存/更新商品，skuId={}", model.getSkuId());
        skuEsRepository.save(model);
    }

    @Override
    public void saveBatch(List<SkuEsModel> models) {
        log.info("[搜索服务] 批量保存商品，数量={}", models.size());
        skuEsRepository.saveAll(models);
    }

    @Override
    public void deleteBySkuId(Long skuId) {
        log.info("[搜索服务] 删除商品，skuId={}", skuId);
        skuEsRepository.deleteById(skuId);
    }
}
