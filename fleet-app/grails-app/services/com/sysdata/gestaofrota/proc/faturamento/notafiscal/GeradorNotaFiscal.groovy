package com.sysdata.gestaofrota.proc.faturamento.notafiscal

import com.sysdata.gestaofrota.Fatura

interface GeradorNotaFiscal {
    void gerarNotaFiscal(Fatura fatura)
}