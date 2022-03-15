package com.talent.market.live.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.talent.market.live.model.Shipping;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShippingMapper extends BaseMapper<Shipping> {


    int updateByPrimaryKeySelective(Shipping record);

}