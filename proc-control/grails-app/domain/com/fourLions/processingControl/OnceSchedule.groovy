package com.fourLions.processingControl

class OnceSchedule extends ExecutionSchedule {

    Date dateTime

    static constraints = {
    }

    @Override
    boolean runNow(Date date) {
        this.dateTime == date
    }
}
