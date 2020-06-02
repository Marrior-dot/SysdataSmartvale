package com.sysdata.xfiles

enum FieldDataType {

    STRING("String"),
    BYTE("Byte"),
    INTEGER("Integer"),
    LONG("Long"),
    FLOAT("Float"),
    DOUBLE("Double"),
    DATE_TIME("DateTime"),
    BOOLEAN("Boolean"),
    BIGDECIMAL("BigDecimal")

    String name

    FieldDataType(name) {
        this.name = name
    }



}