package com.talent.market.live.Controller.manage;

import com.talent.market.live.common.category.AddCategoryCommon;
import com.talent.market.live.common.category.GetCategoryCommon;
import com.talent.market.live.common.category.SetCategoryNameCommon;
import com.talent.market.live.model.User;
import com.talent.market.live.service.CategoryService;
import com.talent.market.live.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author huangzhengwei
 * @desc
 */
@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("add_category.do")
    private ServerResponse addCategory(@RequestBody AddCategoryCommon common, HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        User user=(User) session.getAttribute("manage_user");
        String username = user.getUsername();
        return categoryService.addCategory(common, username);
    }

    @PostMapping("get_category.do")
    public ServerResponse getCategory(@RequestBody GetCategoryCommon categoryCommon,HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }

        return categoryService.getCategory(categoryCommon);
    }


    @PostMapping("set_category_name.do")
    public ServerResponse setCategoryName(@RequestBody SetCategoryNameCommon common,HttpSession session){
        ServerResponse serverResponse = validUser(session);
        if(serverResponse.getStatus()==1||serverResponse.getStatus()==10){
            return serverResponse;
        }
        User user=(User) session.getAttribute("manage_user");
        String username = user.getUsername();
        return categoryService.setCategoryName(common, username);

    }


    public ServerResponse validUser(HttpSession session){
        User user=(User) session.getAttribute("manage_user");
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(10,"请登录");
        }
        if(user.getRole().equals(0)){
            return ServerResponse.createByErrorMessage("权限不足");
        }
        return ServerResponse.createBySuccess();
    }


}
