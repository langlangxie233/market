package com.talent.market.live.common.product;

import lombok.Data;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class ProductListCommon {
    private String categoryId;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private String orderBy;
}
