package com.sysdata.gestaofrota.jobs

import com.sysdata.gestaofrota.Util
import com.fourLions.processingControl.ProcessingService


class DailyProcessJob {

    ProcessingService processingService

    static triggers = {
        cron startDelay: 1000 * 60, cronExpression: "0 0/5 * * * ?"
    }

    def execute() {
        Date date = Util.calcularDataProcessamento()
        processingService.run(date)
    }
}
