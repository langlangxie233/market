package com.talent.market.live.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.talent.market.live.common.user.*;
import com.talent.market.live.dao.UserMapper;
import com.talent.market.live.model.User;
import com.talent.market.live.service.UserService;
import com.talent.market.live.util.ServerResponse;
import com.talent.market.live.util.TokenCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author xiexianlang
 * @desc
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Value(value = "${md5}")
    private String md5;

    @Override
    public ServerResponse login(LoginCommon common) {
        String pass= DigestUtils.md5DigestAsHex((common.getPassword()+md5).getBytes());
        common.setPassword(pass);
        //调用dao
        User user= userMapper.login(common);
        if (user==null){
            return ServerResponse.createByErrorMessage("用户名或密码错误");
        }
        user.setPassword("");
//        UUID.randomUUID().toString().replace("-","");
        if(user.getRole().equals(1)){
            return ServerResponse.createBySuccess("登录成功",user);
        }
        return ServerResponse.createByErrorMessage("权限不足");
    }

    @Override
    public ServerResponse list(UserListCommon common) {
        PageHelper.startPage(common.getPageNum(),common.getPageSize());
        //实例化一个查询条件对象
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role",0);

        List<User> users = userMapper.selectList(wrapper); //select * from talent_User limit
        PageInfo<User> userPageInfo = new PageInfo<User>(users);  //selet count(0) from talent_User
        return ServerResponse.createBySuccess("查询成功",userPageInfo);
    }
        //
    @Override
    public ServerResponse loginClient(LoginCommon common) {
        QueryWrapper<User> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("username",common.getUsername());
        User user=userMapper.selectOne(queryWrapper);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }
        String pass= DigestUtils.md5DigestAsHex((common.getPassword()+md5).getBytes());
        if(!pass.equals(user.getPassword())){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(null);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse register(RegisterUserCommon common) {
        CheckValidCommon checkValidCommon = new CheckValidCommon();

        //校验手机号码
        if(common.getPhone()!=null) {
            checkValidCommon.setStr(common.getPhone());
            checkValidCommon.setType("phone");
            ServerResponse serverResponse = checkValid(checkValidCommon);
            if(serverResponse.getStatus()==1){
                return serverResponse;
            }

        }

        //校验邮箱
        if(common.getEmail()!=null){
            checkValidCommon.setStr(common.getEmail());
            checkValidCommon.setType("email");
            ServerResponse serverResponse = checkValid(checkValidCommon);
            if(serverResponse.getStatus()==1){
                return serverResponse;
            }
        }

        //校验用户名
        if(common.getUsername()!=null){
            checkValidCommon.setStr(common.getUsername());
            checkValidCommon.setType("username");
            ServerResponse serverResponse = checkValid(checkValidCommon);
            if(serverResponse.getStatus()==1){
                return serverResponse;
            }
        }

        //新增
        User user = new User();
        String id=UUID.randomUUID().toString().replace("-","");
        //将comm中内容复制到user
        BeanUtils.copyProperties(common,user);

        //md5加密
        String pass= DigestUtils.md5DigestAsHex((common.getPassword()+md5).getBytes());
        user.setPassword(pass);
        user.setId(id);
        user.setCreateBy(common.getUsername());
        user.setUpdateBy(common.getUsername());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        int insert = userMapper.insert(user);
        if(insert>0){
            return ServerResponse.createBySuccessMessage("用户注册成功");
        }
        return ServerResponse.createByErrorMessage("用户注册失败");
    }

    @Override
    public ServerResponse checkValid(CheckValidCommon common) {
        if(common.getStr()!=null && !"".equals(common.getStr().trim())){
            if(common.getType()!=null && !"".equals(common.getType().trim())){
                QueryWrapper<User> wrapper = new QueryWrapper<>();
                List<User> users=null;
                if("username".equalsIgnoreCase(common.getType())){
                    wrapper.eq("username",common.getStr());
                    users = userMapper.selectList(wrapper);
                    if(users.size()>0){
                        return ServerResponse.createByErrorMessage("用户名已存在");
                    }

                    return ServerResponse.createBySuccessMessage("用户名验证通过");

                }else if("email".equalsIgnoreCase(common.getType())){
                    wrapper.eq("email",common.getStr());
                    users = userMapper.selectList(wrapper);
                    if(users.size()>0){
                        return ServerResponse.createByErrorMessage("邮箱已存在");
                    }

                    return ServerResponse.createBySuccessMessage("邮箱验证通过");

                }else if("phone".equalsIgnoreCase(common.getType())){
                    wrapper.eq("phone",common.getStr());
                    users = userMapper.selectList(wrapper);
                    if(users.size()>0){
                        return ServerResponse.createByErrorMessage("电话已存在已存在");
                    }

                    return ServerResponse.createBySuccessMessage("电话证通过");

                }

                return ServerResponse.createByErrorMessage("类型异常");
            }
            return ServerResponse.createByErrorMessage("传递类型不能为空");
        }

        return ServerResponse.createByErrorMessage("输入信息不能为空");
    }

    @Override
    public ServerResponse forgetGetQuestion(ForgetGetQuestionCommon common) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",common.getUsername());
        User user = userMapper.selectOne(wrapper);
        if(user==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        if(user.getQuestion()==null){
            return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess("查询成功",user.getQuestion());
    }

    @Override
    public ServerResponse forgetCheckAnswer(ForgetCheckAnswerCommon common) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",common.getUsername()).eq("question",common.getQuestion()).eq("answer",common.getAnswer());
        User user = userMapper.selectOne(wrapper);
        if(user==null){
            return ServerResponse.createByErrorMessage("问题答案错误");
        }
        String token=TokenCache.getKey("token_"+user.getUsername());

        if(token==null){
            return ServerResponse.createByErrorMessage("无token信息");
        }
        return ServerResponse.createBySuccess("查询成功",token);
    }

    @Override
    public ServerResponse forgetResetPassword(ForgetResetPasswordCommon common) {
        String key="token_"+common.getUsername();
        String token=TokenCache.getKey(key);
        if(common.getForgetToken().equals(token)){
            String pass= DigestUtils.md5DigestAsHex((common.getPasswordNew()+md5).getBytes());
            //创建一个更新条件
            UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
            userUpdateWrapper.set("password",pass).eq("username",common.getUsername());
            int update = userMapper.update(null, userUpdateWrapper);
            if(update>0){
                return ServerResponse.createBySuccessMessage("密码更新成功");
            }
            return ServerResponse.createByErrorMessage("密码更新失败");
        }

        return ServerResponse.createByErrorMessage("token失效");
    }

    @Override
    public ServerResponse resetPassword(ResetPasswordCommon common, User user) {
        User user1 = userMapper.selectById(user.getId());
        String pass= DigestUtils.md5DigestAsHex((common.getPasswordOld()+md5).getBytes());
        if(pass.equals(user1.getPassword())){
            String passwordNew= DigestUtils.md5DigestAsHex((common.getPasswordNew()+md5).getBytes());
            UpdateWrapper<User> wrapper = new UpdateWrapper<>();
            wrapper.set("password",passwordNew).eq("id",user.getId());
            wrapper.set("update_by",user.getUsername()).eq("id",user.getId());
            wrapper.set("update_time",new Date()).eq("id",user.getId());
            int update = userMapper.update(null, wrapper);
            if(update>0){
                return ServerResponse.createBySuccess();
            }else{
                return ServerResponse.createByErrorMessage("更新失败");
            }

        }
        return ServerResponse.createByErrorMessage("输入的原始密码错误");
    }

    @Override
    public ServerResponse updateInformation(UpdateInformationCommon common, User user) {
        common.setUpdateBy(user.getUsername());
        common.setUpdateTime(new Date());
        Integer count = userMapper.updateInformation(common);
        if(count>0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }
}
