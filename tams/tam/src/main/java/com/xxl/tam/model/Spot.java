package com.xxl.tam.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Spot {
    private String spotId;

    private Integer spotType;

    private String spotName;

    private String spotAddress;

    private Integer spotDays;

    private BigDecimal spotPrice;

    private Integer spotAvailable;

}