package com.sysdata.gestaofrota

class ParametroSistema {

    static String BIN = "BIN"
    static String ANOS_VALIDADE_CARTAO = "ANOS_VALIDADE_CARTAO"
    static String PEDIDO_VALIDADE = "PEDIDO_VALIDADE"
    static String VARIACAO_AUTONOMIA = "VARIACAO_AUTONOMIA"

    String nome
    String valor
    EscopoParametro escopo
    TipoDado tipoDado

    static constraints = {
        escopo nullable: true
        tipoDado nullable: true
    }

    static Integer getValorAsInteger(param) {
        def paramInstance = ParametroSistema.findByNome(param)
        if (paramInstance) {
            return paramInstance.valor as int
        }
        return null
    }


    public void setValor(String valor) {
        if (this.tipoDado in [TipoDado.INTEGER, TipoDado.LONG]) {
            if (Util.isInteger(valor))
                this.valor = valor
            else
                throw new RuntimeException("Valor $valor não é do tipo Inteiro")
        } else if (this.tipoDado in [TipoDado.FLOAT, TipoDado.DOUBLE]) {
            if (Util.isFloat(valor))
                this.valor = valor
            else
                throw new RuntimeException("Valor $valor não é do tipo Ponto Flutuante")
        } else {
            this.valor = valor
        }
    }
}
