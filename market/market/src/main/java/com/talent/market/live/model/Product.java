package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName(value = "talent_product")
@Data
public class Product {
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

    private Date createTime;

    private Date updateTime;


}