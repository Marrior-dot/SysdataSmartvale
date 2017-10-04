package com.sysdata.gestaofrota

import grails.plugins.springsecurity.Secured;

import java.text.SimpleDateFormat

//import org.jpos.iso.ISOChannel
//import org.jpos.iso.ISOMsg
//import org.jpos.iso.channel.ASCIIChannel
//import org.jpos.iso.packager.ISO87APackager;

import com.sysdata.gestaofrota.ca.NsuTermFile;

class FuelTransactionCommand {

    String cartao
    String matricula
    String placa
    String quilometragem
    String estabelecimento
    TipoCombustivel tipoCombustivel
    String valor
    String senha
    String vencimento

    String nsuTerminal
    String nsuHost
    String dataHost
    String horaHost
    boolean autorizada
    String codigoRetorno

    String toString() {
        def flat = ""
        this.properties.each { k, v ->
            flat += "$k:$v\n"
        }
        flat
    }
}

@Secured(['IS_AUTHENTICATED_FULLY'])
class CentralAtendimentoController {

    def authServerService
    def funcionarioService
    def springSecurityService
    def cartaoService

    def index = {}

    def searchCard = {
        [act: params.act, goTo: params.goTo]
    }

    def findFuncionario = {
        String numero = params['cartao']
        if (numero.length() > 0) {
            Cartao cartaoInstance = Cartao.findByNumero(numero)
            if (cartaoInstance) {
                render(view: 'manageCard', model: [cartaoInstance: cartaoInstance, goTo: params.goTo])
            } else {
                flash.errors << "Nenhum cartão localizado com este número"
                render(view: 'searchCard', model: [act: 'findFuncionario', goTo: params.goTo])
            }
        } else {
            flash.errors << "Nº de cartão inválido"
            render(view: 'searchCard', model: [act: 'findFuncionario', goTo: params.goTo])
        }
    }

    def unlockNewCard = {
        Cartao cartaoInstance = Cartao.get(params.long('id'))
        if (cartaoInstance) {
            cartaoInstance = cartaoService.desbloquear(cartaoInstance)
            flash.message = "Cartão DESBLOQUEADO com sucesso"
            render(view: 'manageCard', model: [cartaoInstance: cartaoInstance])
            return
        } else {
            flash.error = "Cartão não encontrado."
            redirect(action: 'searchCard', params: [act: 'findFuncionario', goTo: 'unlockNewCard'])
        }
    }

    def cancelCard = {

        Cartao cartaoInstance = Cartao.get(params.long('id'))
        MotivoCancelamento motivo = MotivoCancelamento.valueOf(params['motivo'])
        if (cartaoInstance && motivo) {
            cartaoInstance = cartaoService.cancelar(cartaoInstance, motivo)
            flash.message = "Cartão ${cartaoInstance.numero} CANCELADO com sucesso. Um novo cartão foi gerado para você."
            render(view: 'manageCard', model: [cartaoInstance: cartaoInstance])
        } else {
            flash.error = "Cartão não encontrado."
            if (motivo == null) flash.error += " Selecione um motivo de cancelamento."
            redirect(action: 'searchCard', params: [act: 'findFuncionario', goTo: 'cancelCard'])
        }
    }


    def settingPriceTransaction = {}
    def fuelTransaction = {}

    def doSettingPriceTransaction = {
        flash.errors = []
        if (params.estabelecimento) {
            if (params.preco) {
                try {
                    def ret = authServerService.handleSettingPriceTransaction(params)
                    if (ret['autorizada'])
                        flash.message = "Transação de Alteração de Preço do Combustível APROVADA. Nsu Aut:${}"
                    else
                        flash.error << "Transação NEGADA. Cod Retorno:${ret['codresp']}"
                } catch (IOException e) {
                    log.error e
                    flash.errors << "Erro na conexão ao Autorizador. Contate suporte"
                }

            } else
                flash.message = "Preço não pode ser nulo"
        } else
            flash.message = "Estabelecimento não pode ser nulo"
        render(view: "settingPriceTransaction")
    }

    def doFuelTransaction = { FuelTransactionCommand cmd ->
        flash.errors = []
        def resp
        try {
            resp = authServerService.handleFuelTransaction(cmd)
            if (resp.autorizada)
                flash.message = "Transação AUTORIZADA. Codigo Autorização:${resp.nsuHost}"
            else
                flash.errors << "Transação NEGADA. Cod Retorno: " + resp.codigoRetorno

        } catch (Exception e) {
            log.error e
            flash.errors << "Falha na comunicação com o Autorizador. Contate suporte "
        }

        render(view: "fuelTransaction", model: [commandInstance: cmd])
    }


    def buscarFuncionarios = {
        flash.errors = []
        println "params: ${params}"
        def numero = params.cartao
        def cartaoDebito = params.cartaoParaTransferir
        def cartaoCredito = params.cartaoParaReceber
        def sucesso = false
        if (cartaoDebito && cartaoCredito) {
            def cartaoInstanceDebito = Cartao.findByNumero(cartaoDebito)
            def cartaoInstanceCredito = Cartao.findByNumero(cartaoCredito)
            def participanteDebito = cartaoInstanceDebito?.funcionario
            def participanteCredito = cartaoInstanceCredito?.funcionario
            if (cartaoInstanceDebito && cartaoInstanceCredito) {
                if (cartaoInstanceDebito.funcionario.conta.saldo > 0) {
                    if (cartaoInstanceDebito.funcionario.unidade == cartaoInstanceCredito.funcionario.unidade) {

                        render(view: 'manageCard', model: [cartaoInstanceDebito: cartaoInstanceDebito, cartaoInstanceCredito: cartaoInstanceCredito, goTo: params.goTo, participanteDebito: participanteDebito, participanteCredito: participanteCredito, sucesso: sucesso])
                    } else {
                        flash.errors << "Os cartões inseridos devem ser do mesmo Rh."
                        render(view: 'searchCards', model: [act: 'buscarFuncionarios', goTo: params.goTo])
                    }
                } else {
                    flash.errors << "Não há saldo disponivel para transferência no cartão $cartaoInstanceDebito.numero    . Saldo: R\$ $cartaoInstanceDebito.funcionario.conta.saldo"
                    render(view: 'searchCards', model: [act: 'buscarFuncionarios', goTo: params.goTo])
                }
            } else if (!cartaoInstanceDebito) {
                flash.errors << "Nenhum cartão localizado na base com o número ${cartaoDebito}"
                render(view: 'searchCards', model: [act: 'buscarFuncionarios', goTo: params.goTo])
            } else if (!cartaoInstanceCredito) {
                flash.errors << "Nenhum cartão localizado na base com o número ${cartaoCredito}"
                render(view: 'searchCards', model: [act: 'buscarFuncionarios', goTo: params.goTo])
            }
        } else {
            flash.errors << "Nº de cartão nulo"
            render(view: 'searchCards', model: [act: 'buscarFuncionarios', goTo: params.goTo])
        }
    }
    def transfSaldo = {
        println "params : $params"
        flash.errors = []
        def cartaoInstanceDebito = Cartao.get(params.cartaoInstanceDebitoId)
        def cartaoInstanceCredito = Cartao.get(params.cartaoInstanceCreditoId)
        def participanteDebito = cartaoInstanceDebito.funcionario
        def participanteCredito = cartaoInstanceCredito.funcionario
        def valorTransf
        def saldoPortador
        def sucesso = false
        if (cartaoInstanceCredito && cartaoInstanceDebito) {
            valorTransf = cartaoInstanceDebito.funcionario.conta.saldo
            saldoPortador = cartaoInstanceCredito.funcionario.conta.saldo
            println "saldo portador debito: $valorTransf - saldo portador credito: $saldoPortador"
            if (centralAtendimentoService.tranferenciaSaldo(cartaoInstanceCredito, cartaoInstanceDebito, valorTransf)) {
                log.info "User:${springSecurityService.currentUser?.name}-Saldo do cartão ${cartaoInstanceDebito.numero} transferido para o cartão ${cartaoInstanceCredito.numero}"
                flash.message = "Saldo do cartão ${cartaoInstanceDebito.numero} transferido para o cartão ${cartaoInstanceCredito.numero} com sucesso."
                sucesso = true
            } else {
                cartaoInstanceCredito.errors.allErrors.each {
                    log.error "Erro cartao ${cartaoInstanceCredito.numero}: $it"
                }
                cartaoInstanceDebito.errors.allErrors.each {
                    log.error "Erro cartao ${cartaoInstanceDebito.numero}: $it"
                }
                flash.errors << "Erro ao receber no cartao ${cartaoInstanceCredito.numero} o saldo transferido do cartão ${cartaoInstanceDebito.numero}!"
            }
        }
        render view: 'manageCard', model: [cartaoInstanceDebito: cartaoInstanceDebito, cartaoInstanceCredito: cartaoInstanceCredito, goTo: params.goTo, participanteDebito: participanteDebito, participanteCredito: participanteCredito, sucesso: sucesso]
    }

}
