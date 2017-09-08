package com.sysdata.gestaofrota

/**
 * Created by acception on 08/09/17.
 */
enum TipoVinculoCartao {

    FUNCIONARIO("Funcionário"),
    VEICULO("Veículo")

    String nome

    TipoVinculoCartao(nome){
        this.nome=nome
    }
}