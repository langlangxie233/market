package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
/**
 * @author xiexianlang
 * @desc
 */
@TableName(value = "talent_category")
@Data
public class Category {
    private String id;

    private String parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;


}