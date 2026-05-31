package com.planb.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.planb.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
