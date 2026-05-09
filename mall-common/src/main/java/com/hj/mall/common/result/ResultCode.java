package com.hj.mall.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),

    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未认证"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),

    // 业务异常 1xxx
    MEMBER_EXIST(1001, "用户已存在"),
    MEMBER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    TOKEN_INVALID(1004, "Token 无效或已过期"),

    PRODUCT_NOT_FOUND(1101, "商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(1102, "库存不足"),

    ORDER_NOT_FOUND(1201, "订单不存在"),
    ORDER_STATUS_ERROR(1202, "订单状态异常"),

    SECKILL_NOT_START(1301, "秒杀未开始"),
    SECKILL_ENDED(1302, "秒杀已结束"),
    SECKILL_SOLD_OUT(1303, "商品已售罄"),
    SECKILL_REPEAT(1304, "重复秒杀"),

    WARE_NOT_FOUND(1401, "库存记录不存在"),
    WARE_STOCK_NOT_ENOUGH(1402, "仓储库存不足"),

    // 系统异常 5xxx
    INTERNAL_ERROR(5000, "系统内部错误"),
    FEIGN_ERROR(5001, "服务调用失败"),
    SEATA_ERROR(5002, "分布式事务异常");

    private final Integer code;
    private final String msg;
}
