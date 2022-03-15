package com.talent.market.live.Controller.client;

import com.talent.market.live.common.order.CreateOrderCommon;
import com.talent.market.live.common.order.DetailClientOrderCommon;
import com.talent.market.live.common.order.OrderClientListCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.service.OrderService;
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
@RequestMapping("/order/")
public class OrderClientController {
    @Autowired
    private OrderService orderService;

    @PostMapping("create.do")
    public ServerResponse createOrder(@RequestBody CreateOrderCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();

        return orderService.createOrder(common,user.getId(), user.getUsername());
    }

    @PostMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();

        return orderService.getOrderCartProduct(user.getId());
    }

    @PostMapping("list.do")
    public ServerResponse orderClientList(@RequestBody OrderClientListCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();
        return orderService.orderClientList(common,user.getId());
    }

    @PostMapping("detail.do")
    public ServerResponse detailClientOrder(@RequestBody DetailClientOrderCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        return orderService.detailClientOrder(common);
    }

    @PostMapping("cancel.do")
    private ServerResponse cancelOrder(@RequestBody DetailClientOrderCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User) serverResponse.getData();

        return orderService.cancelOrder(common,user.getId(), user.getUsername());
    }

    public ServerResponse validUserLogin(HttpSession session){
        User user =(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        return ServerResponse.createBySuccess(user);
    }
}
