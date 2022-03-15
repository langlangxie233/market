package com.talent.market.live.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
/**
 * @author xiexianlang
 * @desc
 */
@TableName(value = "talent_shipping")
@Data
public class Shipping {
    private String id;

    private String userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Integer isDeleted;

    private String createBy;

    private String updateBy;

    private Date createTime;

    private Date updateTime;

}