package com.talent.market.live.common.order;

import lombok.Data;

/**
 * @author huangzhengwei
 * @desc
 */
@Data
public class OrderClientListCommon {
    private Integer pageSize=10;
    private Integer pageNum=1;
    private Integer flag;
}
