package com.talent.market.live.common.user;

import lombok.Data;

import java.util.Date;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class UpdateInformationCommon {
    private String email;

    private String phone;

    private String question;

    private String answer;

    private String id;

    private Integer isDeleted;

    private String updateBy;

    private Date updateTime;
}
