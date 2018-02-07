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
    TipoVinculoCartao vinculoCartao = TipoVinculoCartao.FUNCIONARIO
    TipoCobranca modeloCobranca = TipoCobranca.POS_PAGO
    boolean cartaoComChip = false
    boolean renovarLimite = false

    static hasMany = [
            unidades             : Unidade,
            categoriasFuncionario: CategoriaFuncionario,
            empresas             : PostoCombustivel,
            role                 : Role,
            fechamentos         : Fechamento
    ]

    static constraints = {
        codigo nullable: false, blank: false
    }

    static transients = ['portadoresCount',"corteAberto"]

    String toString() {
        "${codigo} - ${nome}"
//        def flat = ""
//        this.properties.each {
//            flat += "${it}\n"
//        }
//        flat
    }

    int getPortadoresCount() {
        unidades?.sum { it.portadores.size() } ?: 0
    }

    Corte getCorteAberto(){
        Corte.withCriteria(uniqueResult:true) {
            'in'("fechamento",this.fechamentos)
            eq("status",StatusCorte.ABERTO)
        }
    }


}