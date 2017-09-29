package com.sysdata.gestaofrota

abstract class Portador {
    Conta conta = new Conta()

    static hasMany = [cartoes: Cartao]
    static belongsTo = [unidade: Unidade]

    static constraints = {
    }

    static transients = ['cartaoAtivo', 'cartaoAtual', 'saldo']

    Cartao getCartaoAtivo() {
        cartoes.find { it.status == StatusCartao.ATIVO || it.status == StatusCartao.EMBOSSING }
    }

    Cartao getCartaoAtual() {
        cartoes.max { it.dateCreated }
    }

    BigDecimal getSaldo() {
        conta.saldo
    }
}
