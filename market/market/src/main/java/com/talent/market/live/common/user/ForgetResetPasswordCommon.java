package com.talent.market.live.common.user;

import lombok.Data;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class ForgetResetPasswordCommon {
    private String username;
    private String passwordNew;
    private String forgetToken;
}