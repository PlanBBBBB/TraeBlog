package com.planb.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planb.blog.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
