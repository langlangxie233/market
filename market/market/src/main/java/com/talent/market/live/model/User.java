package com.talent.market.live.model;



import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author xiexianlang
 * @desc
 */
@TableName(value = "talent_user")
@Data
public class User {

    private String id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String question;

    private String answer;

    private Integer role;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

}
