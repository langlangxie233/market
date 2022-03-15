package com.talent.market.live.service;

import com.github.pagehelper.PageInfo;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.vo.OrderVo;

import java.util.Map;

/**
 * @author xiexianlang
 * @desc
 */
public interface IOrderService {
    ServerResponse pay(String orderNo, String userId, String path);
    ServerResponse aliCallback(Map<String, String> params);
    ServerResponse queryOrderPayStatus(String userId, String orderNo);
    ServerResponse createOrder(String userId, String shippingId);
    ServerResponse<String> cancel(String userId, String orderNo);
    ServerResponse getOrderCartProduct(String userId);
    ServerResponse<OrderVo> getOrderDetail(String userId, String orderNo);
    ServerResponse<PageInfo> getOrderList(String userId, int pageNum, int pageSize);



    //backend
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);
    ServerResponse<OrderVo> manageDetail(String orderNo);
    ServerResponse<PageInfo> manageSearch(String orderNo, int pageNum, int pageSize);
    ServerResponse<String> manageSendGoods(String orderNo);


}
