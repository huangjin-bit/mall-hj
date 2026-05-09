package com.hj.mall.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hj.mall.auth.entity.Member;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员Mapper
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
