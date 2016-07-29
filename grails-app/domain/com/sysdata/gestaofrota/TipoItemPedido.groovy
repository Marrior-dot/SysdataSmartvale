package com.sysdata.gestaofrota

/**
 * Created by luiz on 28/07/16.
 */
enum TipoItemPedido {

    CARGA("Carga"),
    TAXA("Taxa")

    String nome

    TipoItemPedido(nome){
        this.nome=nome
    }
}