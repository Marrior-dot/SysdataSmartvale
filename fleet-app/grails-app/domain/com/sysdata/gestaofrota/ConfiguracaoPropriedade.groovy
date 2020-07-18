package com.sysdata.gestaofrota

class ConfiguracaoPropriedade {

    String nome
    TipoParticipante tipoParticipante
    String dsl
    boolean ativo = true

    static constraints = {
    }

    static mapping = {
        dsl type: 'text'
    }
}
