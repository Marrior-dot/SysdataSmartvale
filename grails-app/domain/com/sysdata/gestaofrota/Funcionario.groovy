package com.sysdata.gestaofrota

import java.text.DecimalFormat

class Funcionario extends Participante {

    String cpf
    String matricula
    String rg
    Date dataNascimento
    String cnh
    Date validadeCnh
    Telefone telefoneComercial
    CategoriaFuncionario categoria
    CategoriaCnh categoriaCnh
    boolean gestor

    static belongsTo = [unidade: Unidade]
    static embedded = ['telefoneComercial']
    static hasMany = [veiculos: MaquinaFuncionario]
    static transients = ['cartaoAtivo']
    static hasOne=[portador:Portador]

    static constraints = {
        cpf(blank: false, cpf: true)
        matricula(blank: false, validator: { val, obj ->

            def func = Funcionario.withCriteria {
                unidade { eq("id", obj.unidade.id) }
                eq("matricula", val)
            }

            if (func && func.find { it != obj }) return "funcionario.matricula.unica"

        })
        cnh(blank: false)
        portador nullable:true
    }

    Cartao getCartaoAtivo() {
        cartoes.find { it.status == StatusCartao.ATIVO || it.status == StatusCartao.EMBOSSING }
    }

    Cartao cartaoAtual() {
        cartoes.max { it.dateCreated }
    }

    public boolean isAtivoInPedido(PedidoCarga pedidoCarga) {
        if(!pedidoCarga) return false
        ItemPedido item = pedidoCarga.itens.find { it.participante.id == this.id && it.tipo==TipoItemPedido.CARGA}
        item?.ativo
    }

    public BigDecimal valorInPedido(PedidoCarga pedidoCarga, int decimalPlace = 2) {
        if(!pedidoCarga) return Util.toBigDecial(categoria.valorCarga, decimalPlace)
        ItemPedido item = pedidoCarga.itens.find { it.participante.id == this.id && it.tipo==TipoItemPedido.CARGA}
        Double valor = item?.valor ?: categoria.valorCarga
        Util.toBigDecial(valor, decimalPlace)
    }
}
