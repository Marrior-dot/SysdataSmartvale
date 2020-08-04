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
    Date dataCancelamento

    static hasMany = [itens: ItemPedido]
    static transients = ['perfisRecarga','dataCargaClear']

    static constraints = {
        usuario(nullable: true)
        taxa nullable: true
        total nullable: true
        dataCancelamento nullable: true
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

    public def getPerfisRecarga(){
        if (! this.unidade)
            return null
        else if (this.unidade.rh?.vinculoCartao == TipoVinculoCartao.MAQUINA)
            return this.itens*.maquina*.categoria as Set
        else if (this.unidade.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO)
            return this.itens*.participante*.categoria as Set
    }

    public def getDataCargaClear(){
        Date.parse('dd/MM/yyyy', dataCarga)
    }

    boolean isVeiculoInPedido(Veiculo veiculo) {
        ItemPedido item = this.itens.find { it.maquina == veiculo && it.tipo == TipoItemPedido.CARGA }
        return item
    }

    boolean isFuncionarioInPedido(Funcionario funcionario) {
        ItemPedido item = this.itens.find { it.participante == funcionario && it.tipo == TipoItemPedido.CARGA }
        return item
    }

    public BigDecimal valorInPedido(objeto, int decimalPlace = 2) {
        ItemPedido item
        if (objeto instanceof Funcionario)
            item = this.itens.find { it.participante == objeto && it.tipo == TipoItemPedido.CARGA }
        else if (objeto instanceof Veiculo)
            item = this.itens.find { it.maquina == objeto && it.tipo == TipoItemPedido.CARGA }

        Double valor = item?.valor ?: objeto.categoria.valorCarga

        println "Valor Carga: $valor"
        Util.toBigDecimal(valor, decimalPlace)
    }


}
