package com.sysdata.gestaofrota

class Rh extends Empresa {
    String codigo
    Integer validadeCarga = 0
    Integer maximoTrnPorDia = 0
    Integer diasInatividade = 0
    Integer diasToleranciaAtraso = 0
    Integer qtdeContas = 0
    Integer prazoPgtFatura = 0
    BigDecimal taxaPedido = 0
    BigDecimal taxaUtilizacao = 0D
    BigDecimal taxaMensalidade = 0D
    BigDecimal taxaEmissaoCartao = 0D
    BigDecimal taxaReemissaoCartao = 0D
    BigDecimal jurosProRata = 0D
    BigDecimal multaAtraso = 0D
    BigDecimal taxaAdministracao = 0D
    BigDecimal taxaManutencao = 0D
    // TODO: colocar em Portador
//    BigDecimal limiteMensal = 0D
//    BigDecimal limiteDiario = 0D
//    BigDecimal limiteCredito = 0D
    TipoVinculoCartao vinculoCartao = TipoVinculoCartao.FUNCIONARIO
    TipoCobranca modeloCobranca = TipoCobranca.POS_PAGO
    boolean cartaoComChip = false
    boolean renovarLimite = false

    static hasMany = [
            unidades             : Unidade,
            categoriasFuncionario: CategoriaFuncionario,
            empresas             : PostoCombustivel,
            role                 : Role,
            agendamentos         : Agendamento
    ]

    static constraints = {
        codigo nullable: false, blank: false
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