package com.hj.mall.member.mapper;

import com.hj.mall.member.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员Mapper接口
 */
@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}
