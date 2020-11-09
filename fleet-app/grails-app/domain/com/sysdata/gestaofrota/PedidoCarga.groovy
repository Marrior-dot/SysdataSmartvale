package com.sysdata.gestaofrota

class PedidoCarga {

    Date dateCreated
    Unidade unidade
    Integer validade
    User usuario
    Date dataCancelamento
    Fatura fatura
    BigDecimal taxa
    BigDecimal taxaDesconto

    static hasMany = [itens: ItemPedido]

    static transients = ['perfisRecarga', 'dataCargaClear', "totalBruto", "total", "itensCarga"]

    static constraints = {
        usuario(nullable: true)
        dataCancelamento nullable: true
        fatura nullable: true
        taxa nullable: true
        taxaDesconto nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence: 'pedidocarga_seq']
    }

    static hibernateFilters = {
        pedidoPorRh(condition: 'unidade_id in (select u.id from Unidade u where u.rh_id =:rh_id)', types: 'long')
    }

    BigDecimal getTotal() {
        this.itens.sum { it.valor }
    }

    BigDecimal getTotalBruto() {
        return this.itens.sum {
                                if (it.instanceOf(ItemPedidoMaquina) || it.instanceOf(ItemPedidoParticipante))
                                    return it.valor
                                }
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

    public def getItensCarga() {
        return this.itens.findAll { it.tipo == TipoItemPedido.CARGA }
    }


    public def getPerfisRecarga(){
        if (! this.unidade)
            return null
        else if (this.unidade.rh?.vinculoCartao == TipoVinculoCartao.MAQUINA)
            return this.itens.findAll { it.tipo == TipoItemPedido.CARGA }*.maquina*.categoria as Set
        else if (this.unidade.rh?.vinculoCartao == TipoVinculoCartao.FUNCIONARIO)
            return this.itens.findAll { it.tipo == TipoItemPedido.CARGA }*.participante*.categoria as Set
    }

    public def getDataCargaClear(){
        Date.parse('dd/MM/yyyy', dataCarga)
    }

    boolean isVeiculoInPedido(Veiculo veiculo) {
        ItemPedido item = this.itens.find { it.tipo == TipoItemPedido.CARGA && it.maquina == veiculo }
        return item
    }

    boolean isFuncionarioInPedido(Funcionario funcionario) {
        ItemPedido item = this.itens.find { it.tipo == TipoItemPedido.CARGA && it.participante == funcionario }
        return item
    }

    public String valorInPedido(objeto, int decimalPlace = 2) {
        ItemPedido item
        if (objeto instanceof Funcionario)
            item = this.itens.find { it.tipo == TipoItemPedido.CARGA && it.participante == objeto }
        else if (objeto instanceof Veiculo)
            item = this.itens.find { it.tipo == TipoItemPedido.CARGA && it.maquina == objeto  }

        def valor = item?.valor ?: objeto.categoria.valorCarga

        return Util.formatCurrency(valor)

    }


}
