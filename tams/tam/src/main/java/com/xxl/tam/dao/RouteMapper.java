package com.xxl.tam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.tam.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RouteMapper extends BaseMapper<User> {
}