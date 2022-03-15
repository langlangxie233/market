package com.xxl.tam.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xxl.tam.common.user.LoginCommon;
import com.xxl.tam.common.user.RegisterUserCommon;
import com.xxl.tam.common.user.UpdateInformationCommon;
import com.xxl.tam.dao.UserMapper;
import com.xxl.tam.model.User;
import com.xxl.tam.service.UserService;
import com.xxl.tam.util.ServerResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value(value = "md5")
    private String md5;

    @Override
    public ServerResponse loginVisitor(LoginCommon common) {
        QueryWrapper<User> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("user_id",common.getUserId());
        User user=userMapper.selectOne(queryWrapper);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        //md5加密
        String pass= DigestUtils.md5DigestAsHex((common.getPwd()+md5).getBytes());
        //String pass= common.getPwd();
        if(!pass.equals(user.getPwd())){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPwd(null);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse register(RegisterUserCommon common) {
/*        serverResponse = checkValid(checkValidCommon);
        if(serverResponse.getStatus()==1){
            return serverResponse;
        }
    }*/

        //新增
        User user = new User();
        //将comm中内容复制到user
        BeanUtils.copyProperties(common, user);

        //md5加密
        String pass = DigestUtils.md5DigestAsHex((common.getPwd() + md5).getBytes());
        user.setPwd(pass);
        int insert = userMapper.insert(user);
        if (insert > 0) {
            return ServerResponse.createBySuccessMessage("用户注册成功");
        }
        return ServerResponse.createByErrorMessage("用户注册失败");
    }

    @Override
    public ServerResponse updateInformation(UpdateInformationCommon common) {
        userMapper.updateInformation(common);
        return ServerResponse.createBySuccessMessage("用户信息更新成功");
    }
}
