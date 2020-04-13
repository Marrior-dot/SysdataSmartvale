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
            fechamentos          : Fechamento
    ]

    static constraints = {
        codigo nullable: false, blank: false
    }

    static transients = ['portadoresCount', "corteAberto", "funcionariosCount", "veiculosCount"]

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

    int getFuncionariosCount() {
        this.unidades.sum{it.funcionariosCount}?:0
    }

    int getVeiculosCount() {
        this.unidades.sum{it.veiculosCount}?:0
    }

    Corte getCorteAberto() {

        if (!this.fechamentos) throw new RuntimeException("Nao ha dias de fechamento definidos para o programa $this")

        Corte corteAberto = Corte.withCriteria(uniqueResult: true) {
            'in'("fechamento", this.fechamentos)
            eq("status", StatusCorte.ABERTO)
        }

        //Senão houver corte aberto, cria primeiro Corte
        if (!corteAberto) {

            def dataProc = ReferenceDateProcessing.calcuteReferenceDate()
            def diaProc = dataProc[Calendar.DAY_OF_MONTH]
            def mesProc = dataProc[Calendar.MONTH]

            def diaAnt = 1
            Fechamento ciclo, cicloAnterior

            def ordFechList = this.fechamentos.sort { it.diaCorte }

            ordFechList.find { f ->
                if (diaAnt <= diaProc && diaProc < f.diaCorte) {
                    ciclo = f
                    return true
                }
                diaAnt = f.diaCorte
                cicloAnterior = f
                return false
            }
            //Se ciclo não encontrado, volta para o primeiro

            //Calcula Data Prevista pra Corte
            def dataCorte = dataProc
            def datInicCiclo = dataProc
            def cal = dataCorte.toCalendar()
            def calInic = datInicCiclo.toCalendar()

            if (!ciclo) {
                ciclo = ordFechList[0]
                cal.set(Calendar.DAY_OF_MONTH, ciclo.diaCorte)
                cal.set(Calendar.MONTH, mesProc + 1)
            } else {
                cal.set(Calendar.DAY_OF_MONTH, ciclo.diaCorte)
                cal.set(Calendar.MONTH, mesProc)
            }
            dataCorte = cal.time

            //Calcula Data Inicio Ciclo
            if (!cicloAnterior) cicloAnterior = ordFechList[ordFechList.size() - 1]
            if (cicloAnterior.diaCorte > ciclo.diaCorte) calInic.set(Calendar.MONTH, mesProc - 1)
            else calInic.set(Calendar.MONTH, mesProc)
            calInic.set(Calendar.DAY_OF_MONTH, cicloAnterior.diaCorte + 1)
            datInicCiclo = calInic.time

            corteAberto = new Corte()
            corteAberto.with {
                dataPrevista = dataCorte
                dataCobranca = dataCorte + ciclo.diasAteVencimento
                dataInicioCiclo = datInicCiclo
                status = StatusCorte.ABERTO
                fechamento = ciclo
            }
            corteAberto.save flush: true
            log.info "Corte $corteAberto criado"
        }
        corteAberto
    }
}