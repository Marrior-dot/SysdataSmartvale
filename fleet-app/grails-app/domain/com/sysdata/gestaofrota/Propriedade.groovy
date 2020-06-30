package com.sysdata.gestaofrota

class Propriedade {

    String name
    TipoDado tipoDado
    String valor

    static belongsTo = [participante: Participante]

    static transients = ['valorConvertido']


    static constraints = {
    }

    static mapping = {
        id generator: 'sequence', params: [ sequence: 'prop_seq']
    }

    def getValorConvertido() {

        if (! this.valor && this.tipoDado ==! TipoDado.STRING)
            return null

        switch (this.tipoDado) {
            case TipoDado.INTEGER:
                if (this.valor ==~ /d+/)
                    return this.valor as int
                break
            case TipoDado.LONG:
                if (this.valor ==~ /d+/)
                    return this.valor as long
                break
            case TipoDado.FLOAT:
                if (this.valor ==~ /d+/)
                    return this.valor as float
                break
            case TipoDado.DOUBLE:
                if (this.valor ==~ /d+/)
                    return this.valor as double
                break
            default:
                return this.valor
        }
    }

}
