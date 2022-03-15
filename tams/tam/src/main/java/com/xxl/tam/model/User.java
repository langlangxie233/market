package com.xxl.tam.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "tab_user")
public class User {
    @TableId(value = "user_id", type = IdType.NONE)
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