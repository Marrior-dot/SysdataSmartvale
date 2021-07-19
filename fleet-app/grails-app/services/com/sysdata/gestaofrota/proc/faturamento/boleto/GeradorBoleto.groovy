package com.sysdata.gestaofrota.proc.faturamento.boleto

import com.sysdata.gestaofrota.Boleto

interface GeradorBoleto {
    void gerarBoleto(Boleto boleto)
}