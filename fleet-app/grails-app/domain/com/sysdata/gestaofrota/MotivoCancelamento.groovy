package com.sysdata.gestaofrota

enum MotivoCancelamento {
    PERDA("Perda"),
    ROUBO("Roubo"),
    EXTRAVIO("Extravio"),
    DANIFICADO("Danificado"),
    SOLICITACAO_ADM("Solicitação ADM"),
    DEMISSAO("Demissão")

    String nome

    MotivoCancelamento(nome) {
        this.nome = nome
    }

    String getKey(){
        return name()
    }

    @Override
    String toString() {
        return nome
    }
}
	
