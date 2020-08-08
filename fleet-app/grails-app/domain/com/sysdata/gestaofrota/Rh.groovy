package com.sysdata.gestaofrota

class Rh extends Empresa {
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

    @BindUsing({obj, source ->
        Util.parseCurrency(source['multaAtraso'])
    })
    BigDecimal multaAtraso = 0D

    BigDecimal taxaAdministracao = 0D
    BigDecimal taxaManutencao = 0D
    TipoVinculoCartao vinculoCartao = TipoVinculoCartao.FUNCIONARIO
    TipoCobranca modeloCobranca = TipoCobranca.PRE_PAGO
    boolean cartaoComChip = true
    boolean renovarLimite = false

    @BindUsing({obj, source ->
        Util.parseCurrency(source['limiteTotal'])
    })
    BigDecimal limiteTotal = 0D

    static hasMany = [
            unidades             : Unidade,
            categoriasFuncionario: CategoriaFuncionario,
            empresas             : PostoCombustivel,
            role                 : Role,
            fechamentos          : Fechamento
    ]

    static constraints = {
        unidades lazy: false
    }

    static transients = ['portadoresCount', "funcionariosCount", "veiculosCount", "limiteComprometido", "limiteDisponivel"]

    static hibernateFilters = {
        empresaPorUser(condition: 'id=:owner_id', types: 'long')
    }


    static namedQueries = {

        ativos {
            eq("status", Status.ATIVO)
            order("nome")
        }

        ativosPrepago {
            eq("status", Status.ATIVO)
            eq("modeloCobranca", TipoCobranca.PRE_PAGO)
            order("nome")
        }

    }



    String toString() {
        "${this.cnpj} - ${this.nome}"
    }

    int getFuncionariosCount() {
        Funcionario.countFuncionariosRh(this).get()
    }

    int getVeiculosCount() {
        MaquinaMotorizada.countMaquinasRh(this).get()
    }

    BigDecimal getLimiteComprometido() {

        def comprometido = Rh.withCriteria {
            projections {
                sum("port.limiteTotal")
            }
            createAlias("unidades", "unid")
            createAlias("unid.portadores", "port")
            ne("port.status", Status.CANCELADO)
            eq("id", this.id)
        }[0]

        comprometido ?: 0

    }

    BigDecimal getLimiteDisponivel() {

        def disponivel = Rh.withCriteria {
            projections {
                sum("port.saldoTotal")
            }
            createAlias("unidades", "unid")
            createAlias("unid.portadores", "port")
            ne("port.status", Status.CANCELADO)
            eq("id", this.id)
        }[0]
        return disponivel ?: 0

    }


}

import grails.databinding.BindUsing
