package com.fourLions.processingControl

abstract class ExecutionSchedule {

    ExecutionFrequency executionFrequency
    BaseProcessing processing

    static constraints = {
    }

    static mapping = {
        id generator: "sequence", params: [sequence: "execsched_seq"]
    }

    abstract boolean runNow(Date date)

}
