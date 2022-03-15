package com.talent.market.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.talent.market.live.common.shipping.AddShippingCommon;
import com.talent.market.live.common.shipping.DelShippingCommon;
import com.talent.market.live.common.shipping.ListShippingCommon;
import com.talent.market.live.common.shipping.UpdateShippingCommon;
import com.talent.market.live.dao.ShippingMapper;
import com.talent.market.live.model.Shipping;
import com.talent.market.live.service.ShippingService;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xiexianlang
 * @desc
 */
@Service
public class ShippingServiceImpl implements ShippingService {
  @Autowired
   private ShippingMapper shippingMapper;

    @Override
    public ServerResponse addShipping(AddShippingCommon common, String userId) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(common,shipping);
        String id = UUID.randomUUID().toString().replace("-", "");
        shipping.setId(id);
        shipping.setCreateTime(new Date());
        shipping.setUpdateTime(new Date());
        shipping.setUserId(userId);
        int insert = shippingMapper.insert(shipping);
        if(insert>0){
            return ServerResponse.createBySuccess(id);
        }

        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse delShipping(DelShippingCommon common) {
        int deleteById = shippingMapper.deleteById(common.getShippingId());
        if(deleteById>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse updateShipping(UpdateShippingCommon common) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(common,shipping);
        shipping.setUpdateTime(new Date());
        int i = shippingMapper.updateByPrimaryKeySelective(shipping);
        if(i>0){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    @Override
    public ServerResponse selectShippingById(DelShippingCommon common) {
        Shipping shipping = shippingMapper.selectById(common.getShippingId());
        return ServerResponse.createBySuccess(shipping);
    }

    @Override
    public ServerResponse listShipping(ListShippingCommon common, String userId) {
        QueryWrapper<Shipping> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        List<Shipping> shippings = shippingMapper.selectList(wrapper);
        PageInfo<Shipping> pageInfo = new PageInfo<Shipping>(shippings);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
