package com.hj.mall.search.vo;

import com.hj.mall.search.model.SkuEsModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private List<SkuEsModel> products;
    private Long total;
    private Integer totalPages;
    private Integer pageNum;
    private Integer pageSize;
}
