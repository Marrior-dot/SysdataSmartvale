package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.ReferenceDateProcessing

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


        Corte corteAberto=Corte.withCriteria(uniqueResult:true) {
            'in'("fechamento",this.fechamentos)
            eq("status",StatusCorte.ABERTO)
        }

        //Senão houver corte aberto, cria o primeiro corte
        if(!corteAberto){

            def dataProc=ReferenceDateProcessing.calcuteReferenceDate()
            def diaProc=dataProc[Calendar.DAY_OF_MONTH]

            def diaAnt=1

            this.fechamentos.sort{it.diaCorte}.each{f->

                if(diaAnt>=diaProc && diaProc<=f.diaCorte)
                    return f.diaCorte

            }
        }
    }


}