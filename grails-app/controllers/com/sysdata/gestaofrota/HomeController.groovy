package com.sysdata.gestaofrota

import chart.ChartModel
import chart.DataSet
import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class HomeController {

    def springSecurityService

    def index() {
        redirect(action: 'dashboard')
    }

    def dashboard() {

    }

    def dataGraficoResgate(Integer ano) {
        def papel = springSecurityService.currentUser.papel
        papel = papel?.class == Estabelecimento ? papel : null
        def statusTransacaoRede = [StatusTransacaoRede.CONFIRMADA, StatusTransacaoRede.NAO_AUTORIZADA, StatusTransacaoRede.DESFEITA, StatusTransacaoRede.PENDENTE, StatusTransacaoRede.CANCELADA]
        def data = new ChartModel()
        def isMaster =  SpringSecurityUtils.ifAnyGranted('ROLE_PROC')
        statusTransacaoRede.each { status ->
            if ((isMaster || status == StatusTransacaoRede.CONFIRMADA)) {
                def serie = new DataSet(data)
                serie.label = "Valores"
                data.addDataSet(serie)
                def transactions = Transacao.valorMensal(Transacao.TipoValorMensal.MONTH, [ano: ano, status: status, tipo: TipoTransacao.AVISTA, estabelecimento: papel]).sort {
                    it[1]
                }
                transactions.each {
                    serie.addData(com.acception.Util.Util.getStringMes(it[1]), it[0])
                }
            } // end if
        } // end each
        render data as JSON
    } // dataGraficoResgate

    def dataGraficoMesResgate(Integer ano, Integer mes) {
        def papel = springSecurityService.currentUser.papel
        papel = papel?.class == Estabelecimento ? papel : null
        def statusTransacaoRede = [StatusTransacaoRede.CONFIRMADA, StatusTransacaoRede.NAO_AUTORIZADA, StatusTransacaoRede.DESFEITA, StatusTransacaoRede.PENDENTE, StatusTransacaoRede.CANCELADA]
        def data = new ChartModel()
        def isMaster =  SpringSecurityUtils.ifAnyGranted('ROLE_MASTER, ROLE_SUPORTE')
        statusTransacaoRede.each { status ->
            if ((isMaster || status == StatusTransacaoRede.CONFIRMADA)) {
                def serie = new DataSet(data)
                serie.label = "Valores"
                data.addDataSet(serie)

                def transactions = Transacao.valorMensal(Transacao.TipoValorMensal.DAY, [mes: mes, ano: ano, status: status, tipo: TipoTransacao.AVISTA, estabelecimento: papel])
                ChartModel.organizeMissingLabels(transactions, mes).each {
                    serie.addData(it[1].toString(), it[0])
                }
            } // end if
        } // end each
        render data as JSON
    }
}

