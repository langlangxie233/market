package com.talent.market.live.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xiexianlang
 * @desc
 */
@Data
public class FindCartProductSumVo {
    private boolean allChecked;
    private BigDecimal cartTotalPrice;
    private List<FindCartProductVo> listCartVoList;

    private String imageHost;
}
