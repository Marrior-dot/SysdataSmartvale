package com.fourLions.processingControl

class MonthlySchedule extends ExecutionSchedule {

    Byte day
    Byte hour
    Byte minute

    private static final byte APR = 4
    private static final byte JUN = 6
    private static final byte SEP = 9
    private static final byte NOV = 11


    static constraints = {
    }

    private boolean isNow(Date date) {
        this.hour == date[Calendar.HOUR_OF_DAY] && this.minute == date[Calendar.MINUTE]
    }


    @Override
    boolean runNow(Date date) {
        GregorianCalendar cal = GregorianCalendar.instance
        def diaProc = date[Calendar.DAY_OF_MONTH]
        def mesProc = date[Calendar.MONTH] + 1
        def anoProc = date[Calendar.YEAR]
        return ((this.day >= 28 && diaProc >= 28 && cal.isLeapYear(anoProc) && isNow(date))
            ||
            (this.day == 31 &&  diaProc == 30 && mesProc in [APR, JUN, SEP, NOV] && isNow(date))
            ||
            (this.day == diaProc && isNow(date)))

    }
}
