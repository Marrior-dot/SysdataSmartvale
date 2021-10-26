package com.sysdata.commons.dates

class Holiday {

    Integer day
    Integer month
    String description

    static constraints = {
        day nullable: true
    }
}
