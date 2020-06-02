package com.fourLions.processingControl

class DailySchedule extends ExecutionSchedule {

    Byte hour
    Byte minute

    static constraints = {
    }

    @Override
    boolean runNow(Date data) {
        this.hour == data[Calendar.HOUR_OF_DAY] && this.minute == data[Calendar.MINUTE]
    }


}
