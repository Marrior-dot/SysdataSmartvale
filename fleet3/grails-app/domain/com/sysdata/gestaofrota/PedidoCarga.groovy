package com.sysdata.gestaofrota

import java.math.RoundingMode

class PedidoCarga {

    Date dateCreated
    Date dataCarga
    StatusPedidoCarga status = StatusPedidoCarga.NOVO
    Unidade unidade
    Integer validade
    User usuario
    Double taxa = 0D
    Double total = 0D

    static hasMany = [itens: ItemPedido]
    static transients = ['categoriasFuncionario','dataCargaClear']

    static constraints = {
        usuario(nullable: true)
        taxa nullable: true
        total nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'pedidocarga_seq']
    }


    def calcularTotal() {
        this.total = 0D
        //Calcular Total pelos itens
        this.itens.each { item ->
            this.total += item.valor
        }

        this.taxa = this.unidade.rh.taxaPedido
        BigDecimal tx = BigDecimal.valueOf(this.taxa ?: 0D)
        BigDecimal tot = BigDecimal.valueOf(this.total)
        BigDecimal calc = tot * tx / 100D
        calc.setScale(2, RoundingMode.HALF_UP)
        this.total += calc
    }

    public List<Funcionario> funcionarioList(int max) {
        List<ItemPedido> itensInstance = itens as List<ItemPedido>
        max = Math.min(itensInstance?.size() - 1, max - 1)
        itensInstance.sort { it.participante.nome }

        itensInstance[0..max].collect { it.participante as Funcionario }
    }

    public List<Funcionario> funcionarioList() {
        List<ItemPedido> itensInstance = itens as List<ItemPedido>
        int max = itensInstance?.size() - 1
        itensInstance.sort { it.participante.nome }

        itensInstance[0..max].collect { it.participante as Funcionario }
    }

    public def getCategoriasFuncionario(){
        unidade?.rh?.categoriasFuncionario
    }

    public def getDataCargaClear(){
        Date.parse('dd/MM/yyyy', dataCarga)
    }
}
