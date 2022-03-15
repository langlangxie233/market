package com.talent.market.live.service;

import com.talent.market.live.common.cart.AddProductCartCommon;
import com.talent.market.live.common.cart.DeleteProductCartCommon;
import com.talent.market.live.common.cart.SelectCartProductCommon;
import com.talent.market.live.common.cart.UpdateProductCartCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.util.ServerResponse;


public interface CartService {
    ServerResponse addProductCart(AddProductCartCommon cartCommon, User user);

    ServerResponse shopCartList(String userId);

    ServerResponse updateProductCart(UpdateProductCartCommon common,String userId);

    ServerResponse deleteProductCart(DeleteProductCartCommon common, String userId);

    ServerResponse selectCartProduct(SelectCartProductCommon common,String userId);

    ServerResponse unSelectCartProduct(SelectCartProductCommon common, String userId);

    ServerResponse getCartProductCount(String userId);

    ServerResponse selectAllCartProduct(String userId);

    ServerResponse unSelectAllCartProduct(String userId);
}
