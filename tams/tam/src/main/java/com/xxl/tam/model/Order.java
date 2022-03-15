package com.xxl.tam.model;

import lombok.Data;

@Data
public class Order {
    private String orderId;

    private String groupId;

    private Integer isPayed;
}