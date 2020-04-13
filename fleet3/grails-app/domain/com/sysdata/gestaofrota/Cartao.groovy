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

    static belongsTo = [portador: Portador]

    static transients = ['numeroMascarado']

    static constraints = {
        numero(unique: true)
        motivoCancelamento(nullable: true)
        arquivo(nullable: true)
        cvv nullable: false, blank: false, maxSize: 3
        via nullable: false
    }


    def beforeUpdate() {

        def histCartao

        if (status in [StatusCartao.ATIVO, StatusCartao.BLOQUEADO, StatusCartao.CANCELADO]) {
            histCartao = new HistoricoStatusCartao(cartao: this, novoStatus: this.status)
            if (!histCartao.save())
                return false
        }

        return true
    }

    String getNumeroMascarado() {
        Util.maskCard(this.numero)
    }

    @Override
    String toString() {
        return "${numeroMascarado} [${status.nome}]"
    }

    Portador getPortador(boolean has) {

    }
}
