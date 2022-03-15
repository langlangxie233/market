package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@TableName(value = "talent_order")
@Data
public class Order {
    private String id;

    private String orderNo;

    private String userId;

    private String shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;
}