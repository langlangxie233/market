package com.xxl.tam.common.user;

import lombok.Data;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class RegisterUserCommon {

    private String userId;

    private String pwd;

    private String name;

    private String sexId;

    private String ageId;

    private String occupationId;

    private String salaryId;

    private String phone;

    private String address;

    private Integer isDeleted;

    private Integer role;
}
