package com.talent.market.live.service;

import com.talent.market.live.common.order.CreateOrderCommon;
import com.talent.market.live.common.order.DetailClientOrderCommon;
import com.talent.market.live.common.order.OrderClientListCommon;
import com.talent.market.live.util.ServerResponse;


public interface OrderService {
    ServerResponse createOrder(CreateOrderCommon common, String id, String username);

    ServerResponse getOrderCartProduct(String userId);

    ServerResponse orderClientList(OrderClientListCommon common, String userId);

    ServerResponse detailClientOrder(DetailClientOrderCommon common);

    ServerResponse cancelOrder(DetailClientOrderCommon common, String userId, String username);

    ServerResponse sendGoods(DetailClientOrderCommon common);

    ServerResponse manageOrderSearchById(DetailClientOrderCommon common);
}
