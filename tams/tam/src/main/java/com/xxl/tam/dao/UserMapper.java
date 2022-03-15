package com.xxl.tam.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxl.tam.common.user.LoginCommon;
import com.xxl.tam.common.user.UpdateInformationCommon;
import com.xxl.tam.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User login(LoginCommon common);

    Integer updateInformation(UpdateInformationCommon common);
}