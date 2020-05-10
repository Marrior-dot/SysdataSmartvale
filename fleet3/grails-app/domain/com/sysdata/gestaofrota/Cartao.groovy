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

    static transients = ['numeroMascarado', 'numeroFormatado']

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
        if (this.numero.length() > 6) {
            return this.numero[0..3] + "****" + this.numero[(this.numero.length() - 3)..(this.numero.length() - 1)]
        } else "<< NÚMERO IMASCARÁVEL >>"

    }

    private String splitCartao(String num) {
        def corte = (num.length() >= 4) ? 4 : num.length()
        if (corte == 4)
            splitCartao(num[corte..(num.length() - 1)])
        return num[0..corte - 1] + " "
    }

    String getNumeroFormatado() {
        String formatado = splitCartao(this.numero)
        formatado[0..(formatado.length() - 2)]
    }

    @Override
    String toString() {
        return "${numeroMascarado} [${status.nome}]"
    }

    Portador getPortador(boolean has) {

    }
}
