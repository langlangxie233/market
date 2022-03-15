package com.xxl.tam.model;

import lombok.Data;

import java.util.Date;
@Data
public class Group {
    private String groupId;

    private String name;

    private String spotId;

    private String visitorId;

    private String routeId;

    private String grade;

    private Integer days;

    private Integer num;

    private Date date;
}