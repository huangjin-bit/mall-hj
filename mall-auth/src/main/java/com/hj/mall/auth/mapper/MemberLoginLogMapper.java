package com.hj.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.auth.entity.MemberLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录日志Mapper
 */
@Mapper
public interface MemberLoginLogMapper extends BaseMapper<MemberLoginLog> {
}
