package com.hj.mall.product.feign;

import com.hj.mall.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 秒杀服务Feign客户端（包含原coupon功能）
 */
@FeignClient(
        name = "mall-seckill",
        path = "/feign",
        fallbackFactory = CouponFeignFallback.class
)
public interface CouponFeignClient {

    /**
     * 保存SPU积分边界
     */
    @PostMapping("/spu-bounds/save")
    Result<Void> saveSpuBounds(@RequestBody SpuBoundsTO spuBoundsTO);

    /**
     * 保存SKU满减信息
     */
    @PostMapping("/sku-reduction/save")
    Result<Void> saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);

    /**
     * 获取当前启用的首页广告
     */
    @GetMapping("/home-adv/active")
    Result<List<Object>> getActiveAdvs();

    /**
     * 获取当前启用的首页专题
     */
    @GetMapping("/home-subject/active")
    Result<List<Object>> getActiveSubjects();

    /**
     * SPU积分边界TO
     */
    class SpuBoundsTO {
        private Long spuId;
        private Integer buyBounds;
        private Integer growBounds;

        public SpuBoundsTO() {
        }

        public SpuBoundsTO(Long spuId, Integer buyBounds, Integer growBounds) {
            this.spuId = spuId;
            this.buyBounds = buyBounds;
            this.growBounds = growBounds;
        }

        public Long getSpuId() {
            return spuId;
        }

        public void setSpuId(Long spuId) {
            this.spuId = spuId;
        }

        public Integer getBuyBounds() {
            return buyBounds;
        }

        public void setBuyBounds(Integer buyBounds) {
            this.buyBounds = buyBounds;
        }

        public Integer getGrowBounds() {
            return growBounds;
        }

        public void setGrowBounds(Integer growBounds) {
            this.growBounds = growBounds;
        }
    }

    /**
     * SKU满减信息TO
     */
    class SkuReductionTO {
        private Long skuId;
        private Integer fullPrice;
        private Integer reducePrice;
        private Integer fullCount;
        private List<Integer> memberPrice;

        public SkuReductionTO() {
        }

        public SkuReductionTO(Long skuId, Integer fullPrice, Integer reducePrice, Integer fullCount, List<Integer> memberPrice) {
            this.skuId = skuId;
            this.fullPrice = fullPrice;
            this.reducePrice = reducePrice;
            this.fullCount = fullCount;
            this.memberPrice = memberPrice;
        }

        public Long getSkuId() {
            return skuId;
        }

        public void setSkuId(Long skuId) {
            this.skuId = skuId;
        }

        public Integer getFullPrice() {
            return fullPrice;
        }

        public void setFullPrice(Integer fullPrice) {
            this.fullPrice = fullPrice;
        }

        public Integer getReducePrice() {
            return reducePrice;
        }

        public void setReducePrice(Integer reducePrice) {
            this.reducePrice = reducePrice;
        }

        public Integer getFullCount() {
            return fullCount;
        }

        public void setFullCount(Integer fullCount) {
            this.fullCount = fullCount;
        }

        public List<Integer> getMemberPrice() {
            return memberPrice;
        }

        public void setMemberPrice(List<Integer> memberPrice) {
            this.memberPrice = memberPrice;
        }
    }
}
