package com.talent.market.live.common.user;

import lombok.Data;

import java.util.Date;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class ResetPasswordCommon {
    private String passwordOld;

    private String passwordNew;

    private String updateBy;

    private Date updateTime;

}
