package com.talent.market.live.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.talent.market.live.model.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}