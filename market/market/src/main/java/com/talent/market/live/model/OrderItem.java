package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "talent_order_item")
@Data
public class OrderItem {
    private String id;

    private String userId;

    private String orderNo;

    private String productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

}