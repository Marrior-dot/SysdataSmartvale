package com.sysdata.gestaofrota.jobs

import com.sysdata.gestaofrota.proc.cargaPedido.CargaPedidoService
import com.sysdata.gestaofrota.proc.cargaPedido.FaturamentoCargaPedidoService
import com.sysdata.gestaofrota.proc.cartaoProvisorio.ResetEnvioSenhaCartaoProvisorioService

class RecurrentProcessJob {

    ResetEnvioSenhaCartaoProvisorioService resetEnvioSenhaCartaoProvisorioService
    CargaPedidoService cargaPedidoService
    FaturamentoCargaPedidoService faturamentoCargaPedidoService

    static triggers = {
        cron startDelay: 1000 * 60, cronExpression: "0 0/10 6-22 * * ?"
    }

    def execute() {
        resetEnvioSenhaCartaoProvisorioService.execute(new Date().clearTime())
        cargaPedidoService.execute(new Date().clearTime())
        faturamentoCargaPedidoService.execute(new Date().clearTime())
    }
}
