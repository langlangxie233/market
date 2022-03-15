package com.talent.market.live.service;

import com.talent.market.live.common.product.*;
import com.talent.market.live.model.User;
import com.talent.market.live.util.ServerResponse;


public interface ProductService {
    ServerResponse productManageList(ProductManageListCommon common);

    ServerResponse ProductSearchByCondition(ProductSearchByConditionCommon common);

    ServerResponse productDetail(ProductDetailCommon common);

    ServerResponse setSaleStatus(SetSaleStatusCommon common, User user);

    ServerResponse saveOrUpdateProduct(SaveOrUpdateProductCommon common, User user);

    ServerResponse productList(ProductListCommon common);

    ServerResponse productClientDetail(ProductDetailCommon common);

    ServerResponse productClientList(ProductListCommon common);
}
