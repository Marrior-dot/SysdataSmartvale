package com.sysdata.gestaofrota

enum TipoAdministradoraCartao {
    SYSDATA("Sysdata"),
    MAXCARD("Max Card"),
    AMAZONCARD("Amazon Card"),
    LASA("Lasa"),
    BANPARA("Banpara")


    String nome

    TipoAdministradoraCartao(String nome) {
        this.nome = nome
    }

    String getKey() {
        return name()
    }
}
