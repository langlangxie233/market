package com.talent.market.live.common.user;

import lombok.Data;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class RegisterUserCommon {
    private String username;

    private String password;

    private String email;

    private  String phone;

    private String question;

    private String answer;
}
