package com.thunisoft.zsfy.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
class Key {

    private Timestamp timestamp;

    private String sign;

}