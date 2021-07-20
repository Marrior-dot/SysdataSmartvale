package com.sysdata.gestaofrota

enum TipoCartao {

    PADRAO("Padrão"),
    PROVISORIO("Provisório")

    String name

    TipoCartao(name){
        this.name=name
    }

}