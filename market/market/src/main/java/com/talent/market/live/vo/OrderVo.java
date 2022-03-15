package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class OrderVo {

    private String orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private String paymentTypeDesc;
    private Integer postage;

    private Integer status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单的明细
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private String shippingId;
    private String receiverName;

    private ShippingVo shippingVo;


}
