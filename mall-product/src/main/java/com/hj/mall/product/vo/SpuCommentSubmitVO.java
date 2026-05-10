package com.hj.mall.product.vo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpuCommentSubmitVO {

    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @NotNull(message = "spuId不能为空")
    private Long spuId;

    @NotNull(message = "orderItemId不能为空")
    private Long orderItemId;

    private String spuName;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为1")
    @Max(value = 5, message = "评分最高为5")
    private Integer star;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    /** 评论图片/视频 JSON */
    private String resources;

    /** 购买时属性组合 */
    private String spuAttributes;
}
