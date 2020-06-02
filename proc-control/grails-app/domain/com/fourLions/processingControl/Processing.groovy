package com.fourLions.processingControl

class Processing extends BaseProcessing {

    String service
    Byte order

    static belongsTo = [batch: BatchProcessing]

    static constraints = {
        batch nullable: true
    }

    static mapping = {
        order {
            column name: "ord"
        }

    }
}
