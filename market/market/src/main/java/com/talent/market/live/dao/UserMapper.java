package com.talent.market.live.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.talent.market.live.common.user.LoginCommon;
import com.talent.market.live.common.user.UpdateInformationCommon;
import com.talent.market.live.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiexianlang
 * @desc
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User login(LoginCommon common);

    Integer updateInformation(UpdateInformationCommon common);
}
