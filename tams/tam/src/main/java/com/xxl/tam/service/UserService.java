package com.xxl.tam.service;

import com.xxl.tam.common.user.LoginCommon;
import com.xxl.tam.common.user.RegisterUserCommon;
import com.xxl.tam.common.user.UpdateInformationCommon;
import com.xxl.tam.util.ServerResponse;

public interface UserService {

    ServerResponse loginVisitor(LoginCommon common);

    ServerResponse register(RegisterUserCommon common);

    ServerResponse updateInformation(UpdateInformationCommon common);

}
