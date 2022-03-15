package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class ProductDetailVo {
    private String id;

    private String categoryId;

    private String name;

    private String subtitle;

    private String subImages;

    private String detail;

    private String mainImage;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String imageHost;

    private String parentCategoryId;
}
