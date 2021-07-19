package com.fourLions.processingControl

abstract class BaseProcessing {

    String name
    boolean active

    static hasOne = [executionSchedule: ExecutionSchedule]

    static constraints = {
        executionSchedule nullable: true
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "process_seq"]
    }

    boolean runNow(Date date) {
        this.executionSchedule.runNow(date)
    }


}
