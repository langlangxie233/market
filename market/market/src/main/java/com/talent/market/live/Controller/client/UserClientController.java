package com.talent.market.live.Controller.client;

import com.talent.market.live.common.user.*;
import com.talent.market.live.model.User;
import com.talent.market.live.service.UserService;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.util.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author huangzhengwei
 * @desc
 */
@RestController
@RequestMapping("/user/")
public class UserClientController {

    @Autowired
    private UserService userService;

    @PostMapping("login.do")
    public ServerResponse loginClient(@RequestBody LoginCommon common, HttpSession session){
        ServerResponse serverResponse = userService.loginClient(common);
        User user=(User) serverResponse.getData();
        if(user == null){
            return serverResponse;
        }
        session.setAttribute("user",user);

        String key="token_"+user.getUsername();
        TokenCache.setKey(key, UUID.randomUUID().toString());
        return serverResponse;
    }

    @PostMapping("register.do")
    public ServerResponse register(@RequestBody RegisterUserCommon common){

        return userService.register(common);
    }

    @PostMapping("check_valid.do")
    public ServerResponse checkValid(@RequestBody CheckValidCommon common){

        return userService.checkValid(common);
    }

    @PostMapping("get_user_info.do")
    public ServerResponse getUserInfo(HttpSession session){
        User user=(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请先登录");
        }
        return ServerResponse.createBySuccess("查询成功",user);
    }


    @PostMapping("forget_get_question.do")
    public ServerResponse forgetGetQuestion(@RequestBody ForgetGetQuestionCommon common, HttpSession session){

        return userService.forgetGetQuestion(common);
    }


    @PostMapping("forget_check_answer.do")
    public ServerResponse forgetCheckAnswer(@RequestBody ForgetCheckAnswerCommon common){

        return userService.forgetCheckAnswer(common);
    }


    @PostMapping("forget_reset_password.do")
    public ServerResponse forgetResetPassword(@RequestBody ForgetResetPasswordCommon common){

        return userService.forgetResetPassword(common);
    }


    @PostMapping("reset_password.do")
    public ServerResponse resetPassword(@RequestBody ResetPasswordCommon common,HttpSession session){
        User user=(User) session.getAttribute("user");
        if(user==null){
            ServerResponse.createByErrorMessage("请登录");
        }
        return userService.resetPassword(common,user);
    }

    @PostMapping("get_information.do")
    public ServerResponse getInformation(HttpSession session){
        User user=(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请登录");
        }
        return ServerResponse.createBySuccess("查询成功",user);
    }


    @PostMapping("update_information.do")
    public ServerResponse updateInformation(@RequestBody UpdateInformationCommon common,HttpSession session){
        User user=(User) session.getAttribute("user");
        if(user==null){
            return ServerResponse.createByErrorMessage("请先登录");
        }
        common.setId(user.getId());
        return userService.updateInformation(common, user);
    }

    @PostMapping("logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute("user");
        return ServerResponse.createBySuccess();
    }
}
