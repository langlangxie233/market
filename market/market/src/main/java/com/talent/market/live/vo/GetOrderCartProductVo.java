package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class GetOrderCartProductVo {
    private List<OrderItemVo> orderItemVoList;
    private String imageHost;
    private BigDecimal productTotalPrice;
}
