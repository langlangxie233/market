package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class CreateOrderVo {
    private String orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private Integer postage;
    private Integer status;
    private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
    private Date createTime;

    private List<OrderItemVo> orderItemVoList;
    private String shippingId;
    private ShippingVo shippingVo;


}
