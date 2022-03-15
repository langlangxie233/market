package com.talent.market.live.service;

import com.talent.market.live.common.shipping.AddShippingCommon;
import com.talent.market.live.common.shipping.DelShippingCommon;
import com.talent.market.live.common.shipping.ListShippingCommon;
import com.talent.market.live.common.shipping.UpdateShippingCommon;
import com.talent.market.live.util.ServerResponse;


public interface ShippingService {
    ServerResponse addShipping(AddShippingCommon common, String userId);

    ServerResponse delShipping(DelShippingCommon common);

    ServerResponse updateShipping(UpdateShippingCommon common);

    ServerResponse selectShippingById(DelShippingCommon common);

    ServerResponse listShipping(ListShippingCommon common, String userId);
}
