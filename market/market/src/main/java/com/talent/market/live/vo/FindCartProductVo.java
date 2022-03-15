package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class FindCartProductVo {
      private String id;
      private String userId;
      private String productId;
      private Integer quantity;
      private String productName;
      private String productSubtitle;
      private String productMainImage;
      private BigDecimal productPrice;
      private Integer productStatus;
      private BigDecimal productTotalPrice;
      private Integer productStock;
      private Integer productChecked;
      private String limitQuantity;
}
