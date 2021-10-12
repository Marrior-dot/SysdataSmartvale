package com.sysdata.gestaofrota.jobs

import com.sysdata.gestaofrota.proc.cartaoProvisorio.ResetEnvioSenhaCartaoProvisorioService

class EnvioSenhaJob {

    ResetEnvioSenhaCartaoProvisorioService resetEnvioSenhaCartaoProvisorioService

    static triggers = {
        cron startDelay: 1000 * 60, cronExpression: "0 0/5 6-22 * * ?"
    }

    def execute() {
        resetEnvioSenhaCartaoProvisorioService.execute(new Date().clearTime())
    }
}
