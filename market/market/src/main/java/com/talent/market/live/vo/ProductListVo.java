package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class ProductListVo {
    private String id;

    private String categoryId;

    private String name;

    private String subtitle;

//    private String subImages;

    private String mainImage;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String imageHost;

}
