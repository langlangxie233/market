package com.xxl.tam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.xxl.tam.dao")
public class TamApplication {
    @Value(value = "md5")
    private String md5;

    public static void main(String[] args) {
        SpringApplication.run(TamApplication.class, args);
    }

}
