package com.talent.market.live.common.product;

import lombok.Data;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class ProductSearchByConditionCommon {
    private String productName;
    private String productId;
    private Integer pageNum=0;
    private Integer pageSize=10;
}
