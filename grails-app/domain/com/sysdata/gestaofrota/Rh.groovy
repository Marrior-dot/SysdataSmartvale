package com.sysdata.gestaofrota

class Rh extends Empresa {

    String codigo
    Integer validadeCarga
    Integer maximoTrnPorDia
    Integer diasInatividade
    Integer diasToleranciaAtraso
    Integer qtdeContas = 0
    Double taxaPedido = 0
    BigDecimal taxaUtilizacao = 0
    BigDecimal taxaMensalidade = 0
    BigDecimal taxaEmissaoCartao = 0
    BigDecimal taxaReemissaoCartao = 0
    BigDecimal jurosProRata = 0D
    BigDecimal multaAtraso = 0D
    BigDecimal taxaAdministracao = 0D
    BigDecimal taxaManutencao = 0D
    TipoVinculoCartao vinculoCartao = TipoVinculoCartao.FUNCIONARIO
    TipoCobranca modeloCobranca
    boolean cartaoComChip = false
    boolean renavacaoLimite = false

    static hasMany = [
            unidades             : Unidade,
            categoriasFuncionario: CategoriaFuncionario,
            empresas             : PostoCombustivel,
            role                 : Role,
            agendamentos         : Agendamento
    ]

    static constraints = {
        taxaUtilizacao nullable: true
        taxaMensalidade nullable: true
        taxaEmissaoCartao nullable: true
        taxaReemissaoCartao nullable: true
        qtdeContas nullable: true
        cartaoComChip nullable: true
        diasToleranciaAtraso nullable: true
        modeloCobranca nullable: true
    }

    static transients = ['portadoresCount']

    String toString() {
        def flat = ""
        this.properties.each {
            flat += "${it}\n"
        }
        flat
    }

    int getPortadoresCount() {
        unidades?.sum { it.portadores.size() } ?: 0
    }
}