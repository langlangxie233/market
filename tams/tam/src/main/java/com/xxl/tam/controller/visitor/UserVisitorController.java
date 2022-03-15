package com.xxl.tam.controller.visitor;

import com.xxl.tam.common.user.LoginCommon;
import com.xxl.tam.common.user.RegisterUserCommon;
import com.xxl.tam.common.user.UpdateInformationCommon;
import com.xxl.tam.model.User;
import com.xxl.tam.service.UserService;
import com.xxl.tam.util.ServerResponse;
import com.xxl.tam.util.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author xiexianlang
 * @desc
 */
@RestController
@RequestMapping("/visitor/")
public class UserVisitorController {

    @Autowired
    UserService userService;

    @PostMapping("loginVisitor.do")
    public ServerResponse loginVisitor(@RequestBody LoginCommon common, HttpSession session){
        ServerResponse serverResponse = userService.loginVisitor(common);
        User user=(User) serverResponse.getData();
        if(user == null){
            return serverResponse;
        }
        session.setAttribute("user",user);

        String key="token_"+user.getUserId();
        TokenCache.setKey(key, UUID.randomUUID().toString());
        return serverResponse;
    }

    @PostMapping("register.do")
    public ServerResponse register(@RequestBody RegisterUserCommon common){

        return userService.register(common);
    }

    @PostMapping("updateInformation.do")
    public ServerResponse updateInformation(@RequestBody UpdateInformationCommon common){

        return userService.updateInformation(common);
    }

}
