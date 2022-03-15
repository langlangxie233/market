package com.talent.market.live.common.shipping;

import lombok.Data;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class UpdateShippingCommon {
    private String id;
    private String receiverName;
    private String receiverPhone;
    private String receiverMobile;
    private String receiverProvince;
    private String receiverCity;
    private String receiverAddress;
    private String receiverZip;
}
