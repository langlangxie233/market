package com.talent.market.live.common.product;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author xiexianlang
 * @desc
 */
@Data
public class SaveOrUpdateProductCommon {
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

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date updateTime;

    private Date createTime;
}
