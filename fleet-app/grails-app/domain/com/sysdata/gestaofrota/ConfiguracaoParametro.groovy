package com.sysdata.gestaofrota

class ConfiguracaoParametro {

    TipoParticipante tipoParticipante
    String dsl
    boolean ativo = true

    static constraints = {
    }

    static mapping = {
        dsl type: 'text'
    }
}
