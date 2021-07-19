package com.sysdata.gestaofrota

import grails.databinding.BindUsing

class Propriedade {

    String nome

    @BindUsing({obj, source ->
        TipoDado.valueOf(source['tipoDado'])
    })
    TipoDado tipoDado

    String valor

    static belongsTo = [participante: Participante]

    static transients = ['valorConvertido']


    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [ sequence: 'prop_seq']
        table 'propriedade_dinamica'
    }

    def getValorConvertido() {

        if (! this.valor && this.tipoDado != TipoDado.STRING)
            return ""

        def val

        switch (this.tipoDado) {
            case TipoDado.INTEGER:
                if (this.valor ==~ /\d+/)
                    val = this.valor as int
                break
            case TipoDado.LONG:
                if (this.valor ==~ /\d+/)
                    val = this.valor as long
                break
            case TipoDado.FLOAT:
                if (this.valor ==~ /\d+/)
                    val = this.valor as float
                break
            case TipoDado.DOUBLE:
                if (this.valor ==~ /\d+/)
                    val = this.valor as double
                break
            default:
                val = this.valor
        }

        return val
    }

}
