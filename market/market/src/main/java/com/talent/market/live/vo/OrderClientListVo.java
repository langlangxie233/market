package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class OrderClientListVo {
    private String orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private String paymentTypeDesc;
    private Integer postage;
    private  Integer status;
    private String statusDesc;
    private Date paymentTime;
    private Date sendTime;
    private Date endTime;
    private Date closeTime;
    private Date createTime;

    private List<OrderItemVo> orderItemVoList;
    
     private String imageHost;
     private String shippingId;
     private String receiverName;
     private ShippingVo shippingVo;
}
