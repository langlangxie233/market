package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value = "talent_cart")
@Data
public class Cart {
    private String id;

    private String userId;

    private String productId;

    private Integer quantity;

    private Integer checked;

    private Date createTime;

    private Date updateTime;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;




}