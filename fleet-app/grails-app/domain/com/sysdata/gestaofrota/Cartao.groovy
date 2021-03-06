package com.sysdata.gestaofrota

class Cartao {

    String numero
    StatusCartao status = StatusCartao.CRIADO
    Date dateCreated
    Arquivo arquivo
    MotivoCancelamento motivoCancelamento
    String senha
    Date validade
    String cvv
    Integer via = 1
    TipoCartao tipo

    static belongsTo = [portador: Portador, loteEmbossing: LoteEmbossing]

    static hasMany = [relacaoPortador: RelacaoCartaoPortador, resetsSenhaCartao: ResetSenhaCartao]

    static transients = ['numeroMascarado', 'numeroFormatado', "saldoTotal"]

    static constraints = {
        numero(unique: true)
        motivoCancelamento(nullable: true)
        arquivo(nullable: true)
        cvv nullable: false, blank: false, maxSize: 3
        via nullable: false
        loteEmbossing nullable: true
        tipo nullable: true
    }

    static namedQueries = {
        countCartoesAtivosUnidade { Unidade unidade ->
            projections {
                rowCount('id')
            }
            portador {
                eq("unidade", unidade)
            }

        }
    }



    def beforeUpdate() {
        if (this.portador) {
            def histCartao
            if (status in [StatusCartao.ATIVO, StatusCartao.BLOQUEADO, StatusCartao.CANCELADO]) {
                histCartao = new HistoricoStatusCartao(cartao: this, novoStatus: this.status)
                if (!histCartao.save())
                    return false
            }
        }
        return true
    }

    String getNumeroMascarado() {
        if (this.numero.length() > 6) {
            return this.numero[0..5] + "****" + this.numero[(this.numero.length() - 4)..(this.numero.length() - 1)]
        } else "<< NÚMERO IMASCARÁVEL >>"

    }

    private String splitCartao(String num) {
        def corte = (num.length() >= 4) ? 4 : num.length()
        def parte
        if (corte == 4 && num.length() > 4)
            parte = splitCartao(num[corte..(num.length() - 1)])

        return parte ? num[0..(corte - 1)] + " " + parte : num[0..(corte - 1)]
    }

    String getNumeroFormatado() {
        String formatado = splitCartao(this.numero)
        formatado[0..(formatado.length() - 1)]
    }

    @Override
    String toString() {
        return "${numeroFormatado} [${status.nome}]"
    }

    BigDecimal getSaldoTotal() {
        return this.portador.saldoTotal
    }

    RelacaoCartaoPortador getRelacaoPortadorAtiva(Portador portador) {
        return this.relacaoPortador.find { it.portador == portador && it.status == StatusRelacaoCartaoPortador.ATIVA}
    }


}
