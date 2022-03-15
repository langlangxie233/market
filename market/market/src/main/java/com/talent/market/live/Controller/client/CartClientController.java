package com.talent.market.live.Controller.client;

import com.talent.market.live.common.cart.AddProductCartCommon;
import com.talent.market.live.common.cart.DeleteProductCartCommon;
import com.talent.market.live.common.cart.SelectCartProductCommon;
import com.talent.market.live.common.cart.UpdateProductCartCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.service.CartService;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author xiexianlang
 * @desc
 */
@RestController
@RequestMapping("/cart/")
public class CartClientController {
    @Autowired
    private CartService cartService;

    @PostMapping("add.do")
    public ServerResponse addProductCart(@RequestBody AddProductCartCommon cartCommon,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();

        return cartService.addProductCart(cartCommon,user);
    }


    @PostMapping("list.do")
    public  ServerResponse shopCartList(HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return cartService.shopCartList(user.getId());
    }


    @PostMapping("update.do")
    public ServerResponse updateProductCart(@RequestBody UpdateProductCartCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return cartService.updateProductCart(common,user.getId());
    }

    @PostMapping("delete_product.do")
    public ServerResponse deleteProductCart(@RequestBody DeleteProductCartCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return cartService.deleteProductCart(common,user.getId());
    }

    @PostMapping("select.do")
    public ServerResponse selectCartProduct(@RequestBody SelectCartProductCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return cartService.selectCartProduct(common,user.getId());
    }

    @PostMapping("un_select.do")
    public ServerResponse unSelectCartProduct(@RequestBody SelectCartProductCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return cartService.unSelectCartProduct(common,user.getId());
    }


    @PostMapping("get_cart_product_count.do")
    public ServerResponse getCartProductCount(HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        User user=(User) serverResponse.getData();
        return cartService.getCartProductCount(user.getId());
    }


    @PostMapping("select_all.do")
    public ServerResponse selectAllCartProduct(HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        User user=(User) serverResponse.getData();
       return cartService.selectAllCartProduct(user.getId());
    }

    @PostMapping("un_select_all.do")
    public ServerResponse unSelectAllCartProduct(HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        User user=(User) serverResponse.getData();
        return cartService.unSelectAllCartProduct(user.getId());
    }



    public ServerResponse validUserLogin(HttpSession session){
        User user =(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        return ServerResponse.createBySuccess(user);
    }


}