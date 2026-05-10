package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 搜索服务Feign客户端
 */
@FeignClient(
        name = "mall-search",
        fallbackFactory = SearchFeignFallback.class
)
public interface SearchFeignClient {

    /**
     * 商品上架
     */
    @PostMapping("/feign/product/statusUp")
    Result<Void> productStatusUp(@RequestBody List<SkuEsTO> skuEsModels);

    /**
     * SKU搜索模型TO
     */
    class SkuEsTO {
        private Long skuId;
        private Long spuId;
        private String skuTitle;
        private String skuSubtitle;
        private String skuPrice;
        private String skuImg;
        private Integer saleCount;
        private Integer hasStock;
        private Long hotScore;
        private Long brandId;
        private String brandName;
        private String brandImg;
        private Long catalogId;
        private String catalogName;
        private List<Long> attrs;

        public SkuEsTO() {
        }

        // Getters and Setters
        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public Long getSpuId() {
            return spuId;
        }

        public void setSpuId(Long spuId) {
            this.spuId = spuId;
        }

        public String getSkuTitle() {
            return skuTitle;
        }

        public void setSkuTitle(String skuTitle) {
            this.skuTitle = skuTitle;
        }

        public String getSkuSubtitle() {
            return skuSubtitle;
        }

        public void setSkuSubtitle(String skuSubtitle) {
            this.skuSubtitle = skuSubtitle;
        }

        public String getSkuPrice() {
            return skuPrice;
        }

        public void setSkuPrice(String skuPrice) {
            this.skuPrice = skuPrice;
        }

        public String getSkuImg() {
            return skuImg;
        }

        public void setSkuImg(String skuImg) {
            this.skuImg = skuImg;
        }

        public Integer getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(Integer saleCount) {
            this.saleCount = saleCount;
        }

        public Integer getHasStock() {
            return hasStock;
        }

        public void setHasStock(Integer hasStock) {
            this.hasStock = hasStock;
        }

        public Long getHotScore() {
            return hotScore;
        }

        public void setHotScore(Long hotScore) {
            this.hotScore = hotScore;
        }

        public Long getBrandId() {
            return brandId;
        }

        public void setBrandId(Long brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBrandImg() {
            return brandImg;
        }

        public void setBrandImg(String brandImg) {
            this.brandImg = brandImg;
        }

        public Long getCatalogId() {
            return catalogId;
        }

        public void setCatalogId(Long catalogId) {
            this.catalogId = catalogId;
        }

        public String getCatalogName() {
            return catalogName;
        }

        public void setCatalogName(String catalogName) {
            this.catalogName = catalogName;
        }

        public List<Long> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<Long> attrs) {
            this.attrs = attrs;
        }
    }
}
