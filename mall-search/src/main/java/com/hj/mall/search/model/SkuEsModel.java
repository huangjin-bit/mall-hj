package com.hj.mall.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Document(indexName = "mall_product")
public class SkuEsModel {
    @Id
    private Long skuId;

    @Field(type = FieldType.Long)
    private Long spuId;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String skuName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String spuName;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String spuDescription;

    @Field(type = FieldType.Keyword)
    private String skuImg;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Double)
    private BigDecimal originalPrice;

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Integer)
    private Integer publishStatus;

    @Field(type = FieldType.Date)
    private java.time.LocalDateTime createTime;
}
