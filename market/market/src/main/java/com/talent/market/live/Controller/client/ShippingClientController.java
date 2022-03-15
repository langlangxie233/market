package com.talent.market.live.Controller.client;

import com.talent.market.live.common.shipping.AddShippingCommon;
import com.talent.market.live.common.shipping.DelShippingCommon;
import com.talent.market.live.common.shipping.ListShippingCommon;
import com.talent.market.live.common.shipping.UpdateShippingCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.service.ShippingService;
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
@RequestMapping("/shipping/")
public class ShippingClientController {
    @Autowired
    private ShippingService shippingService;

    @PostMapping("add.do")
    public ServerResponse addShipping(@RequestBody AddShippingCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User)serverResponse.getData();
        return shippingService.addShipping(common,user.getId());
    }

    @PostMapping("del.do")
    public ServerResponse delShipping(@RequestBody DelShippingCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        return shippingService.delShipping(common);
    }

    @PostMapping("update.do")
    public ServerResponse updateShipping(@RequestBody UpdateShippingCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        return shippingService.updateShipping(common);
    }

    @PostMapping("select.do")
    public ServerResponse selectShippingById(@RequestBody DelShippingCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }

        return shippingService.selectShippingById(common);
    }


    @PostMapping("list.do")
    public ServerResponse listShipping(@RequestBody ListShippingCommon common,HttpSession session){
        ServerResponse serverResponse = validUserLogin(session);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
        User user=(User)serverResponse.getData();

        return shippingService.listShipping(common,user.getId());
    }

    public ServerResponse validUserLogin(HttpSession session){
        User user =(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        return ServerResponse.createBySuccess(user);
    }



}
