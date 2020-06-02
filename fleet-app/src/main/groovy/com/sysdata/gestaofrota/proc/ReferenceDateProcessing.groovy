package com.sysdata.gestaofrota.proc

class ReferenceDateProcessing {

    static Date calcuteReferenceDate() {

        Date refDate = new Date()
        Calendar cal = Calendar.instance
        cal.time = refDate

        if (cal.get(Calendar.HOUR_OF_DAY) > 0 && cal.get(Calendar.HOUR_OF_DAY) < 12)
            cal.add(Calendar.DAY_OF_MONTH, -1)
        refDate.clearTime()
    }
}
