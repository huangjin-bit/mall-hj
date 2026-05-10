package com.hj.mall.search.controller;

import com.hj.mall.common.result.Result;
import com.hj.mall.search.dto.SearchParam;
import com.hj.mall.search.service.SearchService;
import com.hj.mall.search.vo.SearchResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public Result<SearchResult> search(SearchParam param) {
        log.info("[搜索接口] 收到搜索请求，参数={}", param);
        SearchResult result = searchService.search(param);
        return Result.success(result);
    }
}
