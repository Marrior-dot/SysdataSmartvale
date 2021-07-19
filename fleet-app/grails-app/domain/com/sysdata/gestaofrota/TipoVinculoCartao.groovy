package com.sysdata.gestaofrota

/**
 * Created by acception on 08/09/17.
 */
enum TipoVinculoCartao {

    FUNCIONARIO("Funcionário"),
    MAQUINA("Máquina")

    String nome

    public TipoVinculoCartao(nome) {
        this.nome = nome
    }

    String getKey() {
        return name()
    }

    @Override
    String toString() {
        return nome
    }
}