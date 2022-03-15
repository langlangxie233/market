package com.talent.market.live.service;

import com.talent.market.live.common.user.*;
import com.talent.market.live.model.User;
import com.talent.market.live.util.ServerResponse;


public interface UserService {
    /**
     * 登录
     * @param common
     * @return
     */
    ServerResponse login(LoginCommon common);

    /**
     * 查询用户列表
     * @param common
     * @return
     */
    ServerResponse list(UserListCommon common);

    /**
     * 门户登录
     * @param common
     * @return
     */
    ServerResponse loginClient(LoginCommon common);

    ServerResponse register(RegisterUserCommon common);

    ServerResponse checkValid(CheckValidCommon common);

    ServerResponse forgetGetQuestion(ForgetGetQuestionCommon common);

    ServerResponse forgetCheckAnswer(ForgetCheckAnswerCommon common);

    ServerResponse forgetResetPassword(ForgetResetPasswordCommon common);

    ServerResponse resetPassword(ResetPasswordCommon common, User user);

    ServerResponse updateInformation(UpdateInformationCommon common, User user);
}
