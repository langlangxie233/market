package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class OrderItemVo {
    private String orderNo;
    private String productId;
    private String productName;
    private String productImage;
    private BigDecimal currentUnitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Date createTime;
}
