package com.xxl.tam.common.user;

import lombok.Data;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class LoginCommon {

    private String userId;

    private String pwd;

    private Integer isDeleted;

    private Integer role;
}
