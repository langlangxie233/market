package com.talent.market.live.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.talent.market.live.common.product.SaveOrUpdateProductCommon;
import com.talent.market.live.model.Product;
import org.apache.ibatis.annotations.Mapper;
/**
 * @author xiexianlang
 * @desc
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    Integer updateByPrimaryKeySelective(SaveOrUpdateProductCommon common);
}
