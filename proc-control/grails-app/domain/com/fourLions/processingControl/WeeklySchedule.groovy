package com.fourLions.processingControl

class WeeklySchedule extends ExecutionSchedule {

    Byte weekDay
    Byte hour
    Byte minute

    static constraints = {
    }

    @Override
    boolean runNow(Date date) {
        this.weekDay == date[Calendar.DAY_OF_WEEK] && this.hour == date[Calendar.HOUR_OF_DAY] && this.minute == date[Calendar.HOUR_OF_DAY]
    }

}
