package com.talent.market.live.Controller.manage;

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
@RequestMapping("/manage/order/")
@RestController
public class OrderManageController {

    @Autowired
    private OrderService orderService;

    @PostMapping("list.do")
    public ServerResponse orderProductList(@RequestBody OrderClientListCommon common, HttpSession session){

        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        return orderService.orderClientList(common,null);
    }


    @PostMapping("search.do")
    public ServerResponse orderProductSearch(@RequestBody DetailClientOrderCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        return orderService.manageOrderSearchById(common);

    }

    @PostMapping("detail.do")
    public ServerResponse orderProductDetail(@RequestBody DetailClientOrderCommon common,HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        User user=(User) serverResponse.getData();

        return orderService.detailClientOrder(common);
    }

    @PostMapping("send_goods.do")
    public ServerResponse sendGoods(@RequestBody DetailClientOrderCommon common,HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        return orderService.sendGoods(common);
    }


    //检测用户是否登录且用户是否是管理员
    public ServerResponse validUser(HttpSession session){
        User user=(User) session.getAttribute("manage_user");
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(10,"请登录");
        }
        if(user.getRole().equals(0)){
            return ServerResponse.createByErrorMessage("权限不足");
        }
        return ServerResponse.createBySuccess(user);
    }

}
