package com.sysdata.gestaofrota.proc.faturamento.ext

/**
 * Created by acception on 06/03/18.
 */
interface ExtensaoFaturamento {

    void gerarLancamento(Map ctx)
    void calcularValor(Map ctx)
}