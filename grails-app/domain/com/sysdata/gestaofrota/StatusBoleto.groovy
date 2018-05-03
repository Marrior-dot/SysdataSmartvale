package com.sysdata.gestaofrota

/**
 * Created by luiz on 28/03/18.
 */
enum StatusBoleto {

    CRIADO("Criado"),
    REMESSA("Remessa"),
    PAGO("Pago"),
    CANCELADO("Cancelado")

    String nome

    StatusBoleto(nome){
        this.nome=nome
    }
}