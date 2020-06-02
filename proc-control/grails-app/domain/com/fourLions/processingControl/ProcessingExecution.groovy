package com.fourLions.processingControl

class ProcessingExecution {

    Processing processing
    Date startTime
    Date endTime
    Double progress
    ExecutionStatus executionStatus = ExecutionStatus.CREATED
    String details


    static constraints = {
        startTime nullable: true
        endTime nullable: true
        progress nullable: true
        details nullable: true
    }

    static mapping = {
        id generator: "sequence", params: ["sequence": "procexec_seq"]
        details type: 'text'
    }

}
