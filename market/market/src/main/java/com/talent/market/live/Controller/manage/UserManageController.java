package com.talent.market.live.Controller.manage;


import com.talent.market.live.common.user.LoginCommon;
import com.talent.market.live.common.user.UserListCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.service.UserService;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author huangzhengwei
 * @desc
 */
@RequestMapping("/manage/user/")
@RestController
public class UserManageController {
    @Autowired
    private UserService userService;

    @PostMapping("login.do")
    public ServerResponse login(@RequestBody LoginCommon common, HttpSession session){
        //调用service，并传递参数
        ServerResponse serverResponse=userService.login(common);
        User user =(User) serverResponse.getData();
        session.setAttribute("manage_user",user);
        return serverResponse;
    }

    /**
     * 查询用户列表
     * @param common
     * @param session
     * @return
     */
    @PostMapping("list.do")
    public ServerResponse list(@RequestBody UserListCommon common,HttpSession session){
        User user=(User) session.getAttribute("manage_user");
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(10,"请先登录");
        }

        if(0==user.getRole()){
            return ServerResponse.createBySuccessMessage("权限不足");
        }

        return userService.list(common);
    }

    @GetMapping("getSessionInfo")
    public User getSessionInfo(HttpSession session){
        User user =(User) session.getAttribute("manage_user");
        return user;
    }

}
