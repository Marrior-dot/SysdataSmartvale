package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.TransacaoException
import grails.orm.PagedResultList

class TransacaoCommand {
    String codigoEstabelecimento
    String placa
    String numeroCartao
    BigDecimal valor
    String senha
    Veiculo veiculo
    Cartao cartao
    Estabelecimento estabelecimento
    String combustivel
    Long quilometragem
    BigDecimal precoUnitario
    Long ultimaQuilometragem
}

class TransacaoController extends BaseOwnerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def transacaoService
    def autorizadorService
    def estornoService

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        final Map paginacao = [
                max   : params.int('max') ?: 10,
                offset: params.int('offset') ?: 0,
                sort  : 'id',
                order : 'desc'
        ]
        final Map filtro = [
                dataInicial: params.date('dataInicial', 'dd/MM/yyyy'),
                dataFinal: params.date('dataFinal', 'dd/MM/yyyy')?.plus(1),
                numeroCartao: params.numeroCartao,
                terminal: params.terminal,
                nsu: params.int('nsu'),
                tipo: params.tipo ? TipoTransacao.valueOf(params.tipo.toString()) : null,
                tipos: [
                                            TipoTransacao.COMBUSTIVEL,
                                            TipoTransacao.SERVICOS,
                                            TipoTransacao.CANCELAMENTO,
                                            TipoTransacao.CANCELAMENTO_COMBUSTIVEL
                        ],
                statusRede: params.statusRede
        ]

        final PagedResultList transacoes = transacaoService.pesquisar(getCurrentUser()?.owner, filtro, paginacao)
        if (filtro.dataFinal)
            filtro.dataFinal -= 1
        [transacaoInstanceList: transacoes as List<Transacao>, transacaoInstanceTotal: transacoes.totalCount] + filtro
    }

    def listAdmin() {
        final Map paginacao = [
                max   : params.int('max') ?: 10,
                offset: params.int('offset') ?: 0,
                sort  : 'id',
                order : 'desc'
        ]
        final Map filtro = [
                dataInicial          : params.date('dataInicial', 'dd/MM/yyyy'),
                dataFinal            : params.date('dataFinal', 'dd/MM/yyyy')?.plus(1),
                numeroCartao         : params.numeroCartao,
                codigoEstabelecimento: params.codigoEstabelecimento,
                nsu                  : params.int('nsu'),
                tipo                 : params.tipo ? TipoTransacao.valueOf(params.tipo.toString()) : null,
                tipos                : [
                        TipoTransacao.CONSULTA_PRECOS,
                        TipoTransacao.CONFIGURACAO_PRECO,
                        TipoTransacao.CARGA_SALDO,
                        TipoTransacao.TRANSFERENCIA_SALDO
                ]
        ]
        final PagedResultList transacoes = transacaoService.pesquisar(getCurrentUser()?.owner, filtro, paginacao)
        if (filtro.dataFinal) filtro.dataFinal -= 1
        [transacaoInstanceList: transacoes as List<Transacao>, transacaoInstanceTotal: transacoes.totalCount] + filtro
    }

    def listPendentes() {
        final Map paginacao = [
                max   : params.int('max') ?: 10,
                offset: params.int('offset') ?: 0,
                sort  : 'id',
                order : 'desc'
        ]
        final Map filtro = [
                dataInicial          : params.date('dataInicial', 'dd/MM/yyyy'),
                dataFinal            : params.date('dataFinal', 'dd/MM/yyyy')?.plus(1),
                numeroCartao         : params.numeroCartao,
                codigoEstabelecimento: params.codigoEstabelecimento,
                nsu                  : params.int('nsu'),
                statusControle       : StatusControleAutorizacao.PENDENTE
        ]
        final PagedResultList transacoes = transacaoService.pesquisar(getCurrentUser()?.owner, filtro, paginacao)
        if (filtro.dataFinal) filtro.dataFinal -= 1
        [transacaoInstanceList: transacoes as List<Transacao>, transacaoInstanceTotal: transacoes.totalCount] + filtro
    }

    def alterarStatus() {
        final String tipo = params.tipoAlteracao.toString().toLowerCase()
        final List<Long> idsTransacoes = params.list('transacoes')
        if (idsTransacoes?.size() > 0) {
            if (tipo == 'confirmacao') {
                transacaoService.confirmar(idsTransacoes)
                flash.message = "Transações confirmadas."
            } else if (tipo == 'desfazimento') {
                transacaoService.desfazer(idsTransacoes)
                flash.message = "Transações desfeitas."
            }
        } else {
            flash.error = "Nenhuma Transação foi selecionada."
        }


        redirect(action: 'listPendentes')
    }

    def show() {
        def transacaoInstance = Transacao.get(params.id)
        if (!transacaoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        } else {
            [transacaoInstance: transacaoInstance]
        }
    }

    def edit() {
        def transacaoInstance = Transacao.get(params.id)
        if (!transacaoInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        } else {
            return [transacaoInstance: transacaoInstance]
        }
    }

    def update() {
        def transacaoInstance = Transacao.get(params.id)
        if (transacaoInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (transacaoInstance.version > version) {

                    transacaoInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'transacao.label', default: 'Transacao')] as Object[], "Another user has updated this Transacao while you were editing")
                    render(view: "edit", model: [transacaoInstance: transacaoInstance])
                    return
                }
            }
            transacaoInstance.properties = params
            if (!transacaoInstance.hasErrors() && transacaoInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'transacao.label', default: 'Transacao'), transacaoInstance.id])}"
                redirect(action: "show", id: transacaoInstance.id)
            } else {
                render(view: "edit", model: [transacaoInstance: transacaoInstance])
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete() {
        def transacaoInstance = Transacao.get(params.id)
        if (transacaoInstance) {
            try {
                transacaoInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'transacao.label', default: 'Transacao'), params.id])}"
            redirect(action: "list")
        }
    }

    def agendarAll() {
        flash.errors = []
        Transacao.withTransaction {
            def ret = transacaoService.agendarAll()
            if (!ret.ok)
                flash.message = "Agendamento executando com SUCESSO"
            else
                flash.errors = ret.msg
        }
        redirect(action: 'list')
    }

    def simulador() {}

    def autorizar(TransacaoCommand cmd) {
        flash.errors = []
        def transacaoInstance
        Transacao.withTransaction {
            try {
                def retorno = autorizadorService.autorizar(cmd)
                transacaoInstance = retorno.transacaoInstance
                if (transacaoInstance.autorizada)
                    flash.message = "Transacao ${transacaoInstance.nsu} AUTORIZADA"
                else {
                    flash.errors << "NÃO AUTORIZADA"
                    flash.errors << retorno.retorno
                }
            } catch (TransacaoException e) {
                log.error e.message
                flash.errors << "ERRO"
                flash.errors << e.message
            }

        }
        render(view: "simulador", model: [transacaoInstance: transacaoInstance])
    }

    def estornar() {
        flash.errors = []
        def transacaoInstance = Transacao.get(params.long('id'))
        try {
            estornoService.estornarTransacao(transacaoInstance)
        } catch (TransacaoException e) {
            log.error e
            flash.errors << "ERRO"
            flash.errors << e

        }
        redirect action: "list", params: params

    }
}
