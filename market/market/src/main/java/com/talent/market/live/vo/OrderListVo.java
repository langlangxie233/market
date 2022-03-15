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
public class OrderListVo {

    private String orderNo;

    private String shippingId;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;

    private Integer postage;

    private Integer status;

    private String statusDesc;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVo> orderItemVoList;

    private ShippingVo shippingVo;

    String imageHost;

    String receiverName;
}
