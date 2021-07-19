package com.sysdata.gestaofrota

/**
 * Created by acception on 07/02/18.
 */
enum StatusFaturamento {

    NAO_FATURADO("Não Faturado"),
    FATURADO("Faturado")

    String nome

    StatusFaturamento(nome){
        this.nome=nome
    }

}