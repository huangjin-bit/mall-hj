package com.hj.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.auth.entity.MemberAuth;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员认证Mapper
 */
@Mapper
public interface MemberAuthMapper extends BaseMapper<MemberAuth> {
}
