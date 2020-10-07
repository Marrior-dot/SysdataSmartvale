package com.sysdata.gestaofrota

import grails.converters.JSON

class PedidoCargaController {
    def exportService

    PedidoCargaService pedidoCargaService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {

        def criteria = {
            if (params.searchDataCarga) {
                Date beginDay = params.date('searchDataCarga','dd/MM/yyyy').clearTime()
                println beginDay
                eq('dataCarga', beginDay)

            }

            if (params.searchDataPedido) {
                Date beginDay = params.date('searchDataPedido','dd/MM/yyyy').clearTime()
                gt('dateCreated', beginDay)
                lt('dateCreated', beginDay+1)
            }


            if (params?.searchStatus) {
                eq('status', StatusPedidoCarga.valueOf(params.searchStatus.toString().toUpperCase()))
            }
        }

        params.max = Math.min(params?.int('max') ?: 10, 100)
        params.sort = "id"
        params.order = "desc"

        def pedidoCargaInstanceList = PedidoCarga.createCriteria().list(params, criteria)
        def pedidoCargaInstanceCount = PedidoCarga.createCriteria().count(criteria)

        [pedidoCargaInstanceList : pedidoCargaInstanceList,
         pedidoCargaInstanceCount: pedidoCargaInstanceCount,
         //unidadeInstance         : unidadeInstance,
         statusPedidoCarga       : StatusPedidoCarga.values(),
         searchDataPedido        : params?.searchDataPedido,
         searchDataCarga         : params?.searchDataCarga,
         searchUnidade           : params?.searchUnidade,
         searchStatus            : params?.searchStatus]
    }

    def create() {
        [pedidoCargaInstance: new PedidoCarga()]
    }

    def save() {
        PedidoCarga pedidoCarga = new PedidoCarga(params)
        def ret = pedidoCargaService.save(pedidoCarga, params)
        if (ret.success) {
            flash.message = ret.message
            redirect(action: "show", id: pedidoCarga.id)
        } else {
            flash.errors = ret.message
            redirect(action: 'create')
        }
    }

    def show() {

        PedidoCarga pedidoCarga = PedidoCarga.get(params.id as long)

        def totalPedido = 0
        pedidoCarga?.itens.each {
            totalPedido += it.valor
        }
        if (! pedidoCarga) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
            return;
        }
        [pedidoCargaInstance: pedidoCarga]
    }

    def edit() {
        def pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        if (!pedidoCargaInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
        [pedidoCargaInstance: pedidoCargaInstance]
    }

    def update() {
        def pedidoCarga = PedidoCarga.get(params.long('id'))
        if (!pedidoCarga) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'Pedido de Carga'), params.id])}"
            redirect(action: "list")
        }
        if (pedidoCarga.version > params.long('version')) {
            flash.errors = "Outro usuário modificou esse pedido de carga enquanto você estava o editando."
            redirect(action: 'edit', id: pedidoCarga.id)
            return;
        }
        Unidade unidadeInstance = Unidade.get(params.long('unidade_id'))
        if (!unidadeInstance) {
            flash.errors = "Unidade não selecionada"
            redirect(action: 'list')
            return;
        }
        Date dataCarga = Date.parse("dd/MM/yyyy", params.dataCarga.toString())
        if (!dataCarga) {
            flash.errors = "Data de carga não definida"
            redirect(action: 'edit', id: pedidoCarga.id)
            return;
        }
        if (dataCarga < new Date() - 1) {
            flash.errors = "Data não pode ser inferior a hoje"
            redirect(action: 'edit', id: pedidoCarga.id)
            return;
        }

        def funcionariosAtivosIds = params.list('funcionariosAtivos')?.collect { it as Long }
        def funcionariosInativosIds = params.list('funcionariosInativos')?.collect { it as Long }

        pedidoCarga.dataCarga = dataCarga.clearTime()
        pedidoCarga.taxa = unidadeInstance?.rh?.taxaPedido

        if (funcionariosAtivosIds.size() > 0 || funcionariosInativosIds.size() > 0) {
            pedidoCarga.total = 0D
            boolean needSave = false

            funcionariosAtivosIds.each { idFuncionario ->
                ItemPedido itemPedido = pedidoCarga.itens.find { it.participante.id == idFuncionario }
                //caso ainda não exista um item pedido para um funcionario selecionado, cria-lo
                if (!itemPedido) {
                    Funcionario funcionario = Funcionario.get(idFuncionario)

                    itemPedido = new ItemPedido()
                    itemPedido.participante = funcionario
                    itemPedido.valor = params?.double("valorCarga[${funcionario.id}]") ?: funcionario?.categoria?.valorCarga
                    itemPedido.sobra = 0.0D
                    itemPedido.ativo = !funcionariosInativosIds.contains(funcionario.id)
                    itemPedido.tipo = TipoItemPedido.CARGA
                    itemPedido.save(flush: true)

                    pedidoCarga.addToItens(itemPedido)
                    needSave = true
                }
            }

            if (needSave) pedidoCarga.save(flush: true)


            pedidoCarga.itens.each { itemPedido ->
                itemPedido.valor = params?.double("valorCarga[${itemPedido.participante.id}]") ?: itemPedido.valor
                if (funcionariosAtivosIds.contains(itemPedido.participante.id))
                    itemPedido.ativo = true
                else if (funcionariosInativosIds.contains(itemPedido.participante.id))
                    itemPedido.ativo = false

                itemPedido.save(flush: true)
                if (itemPedido.ativo) pedidoCarga.total += itemPedido.valor
            }

            Double taxa = pedidoCarga?.unidade?.rh?.taxaPedido ?: 0.0D
            pedidoCarga.total += taxa / 100 * pedidoCarga.total
        }

        pedidoCarga.status = StatusPedidoCarga.NOVO
        if (!pedidoCarga.save(flush: true)) {
            flash.errors = pedidoCarga.errors
            redirect(action: 'edit', id: pedidoCarga.id)
            return;
        } else flash.message = "Pedido alterado com sucesso!"

        redirect(action: 'show', id: pedidoCarga.id)
    }

    def delete = {
        def pedidoCargaInstance = PedidoCarga.get(params.long('id'))

        if (pedidoCargaInstance) {
            try {

                //Altera status de lançamentos de taxas para a efetivar
                pedidoCargaInstance.itens.findAll {
                    it.tipo == TipoItemPedido.TAXA && it.lancamento.status == StatusLancamento.EFETIVADO
                }.each { i ->
                    def lc = i.lancamento
                    lc.status = StatusLancamento.A_FATURAR
                    lc.save(flush: true)
                }


                pedidoCargaInstance.delete(flush: true)

                log.debug "Pedido ${pedidoCargaInstance.id} removido"

                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.errors = "${message(code: 'default.not.deleted.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        } else {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }
    }


    def listVeiculos() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        if (! pedidoCargaInstance)
            pedidoCargaInstance = new PedidoCarga()
        CategoriaFuncionario categoriaInstance = CategoriaFuncionario.get(params.long('categoria'))

        def criteria = {

            if (! categoriaInstance) {
                def unidId = params.unidade ? params.long('unidade') : null
                unidade {
                    idEq(unidId)
                }
            }

            if (params?.searchPlaca && params?.searchPlaca.length() > 0) {
                eq('placa', params.searchPlaca)
            }

            if (categoriaInstance) {
                eq('categoria', categoriaInstance)
            }

            if (pedidoCargaInstance?.itens.findAll { it.tipo == TipoItemPedido.CARGA }) {
                'in'('id', pedidoCargaInstance.itens.findAll { it.tipo == TipoItemPedido.CARGA }*.maquina.id)
            }
        }


        params.sort = 'placa'

        render(template: '/pedidoCarga/veiculoList',
                model: [pedidoCargaInstance     : pedidoCargaInstance,
                        veiculoInstanceList     : Veiculo.createCriteria().list(params, criteria),
                        veiculoInstanceCount    : Veiculo.createCriteria().count(criteria),
                        categoriaInstance       : categoriaInstance,
                        action                  : params.actionView])

    }


    def listFuncionarios() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        if (! pedidoCargaInstance)
            pedidoCargaInstance = new PedidoCarga()

        CategoriaFuncionario categoriaInstance = CategoriaFuncionario.get(params.long('categoria'))

        def criteria = {

            if (! categoriaInstance) {
                def unidId = params.unidade ? params.long('unidade') : null
                unidade {
                    idEq(unidId)
                }
            }

            if (params?.searchMatricula && params?.searchMatricula.length() > 0) {
                eq('matricula', params.searchMatricula)
            }

            if (params?.searchNome && params?.searchNome.length() > 0) {
                ilike('nome', "${params.searchNome}%")
            }

            if (params?.searchCpf && params?.searchCpf.length() > 0) {
                eq('cpf', params.searchCpf)
            }

            if (categoriaInstance) {
                eq('categoria', categoriaInstance)
            }

            if (pedidoCargaInstance?.itens.findAll { it.tipo == TipoItemPedido.CARGA }) {
                'in'('id', pedidoCargaInstance.itens.findAll { it.tipo == TipoItemPedido.CARGA }*.participante.id)
            }
        }

        params.sort = 'nome'

        def funcList = Funcionario.createCriteria().list(params, criteria)
        def funcCount = Funcionario.createCriteria().count(criteria)

        render(template: '/pedidoCarga/funcionarioList',
                model: [pedidoCargaInstance     : pedidoCargaInstance,
                        funcionarioInstanceList : funcList,
                        funcionarioInstanceCount: funcCount,
                        categoriaInstance       : categoriaInstance,
                        action                  : params.actionView])
    }

    def getAllFuncionariosIds() {
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        CategoriaFuncionario categoriaInstance = CategoriaFuncionario.get(params.long('categoria'))

        def funcionarioInstanceList = Funcionario.createCriteria().list {
            if (categoriaInstance) {
                eq('categoria', categoriaInstance)
            }

            if (pedidoCargaInstance?.itens.findAll { it.tipo == TipoItemPedido.CARGA }) {
                'in'('id', pedidoCargaInstance.itens.findAll { it.tipo == TipoItemPedido.CARGA }*.participante.id)
            }

            order('nome')

            projections {
                property('id')
            }
        }

        def model = [idsFuncionarios: funcionarioInstanceList, valorCategoria: Util.toBigDecimal(categoriaInstance?.valorCarga).toString()]
        render model as JSON
    }

    def gerarPlanilha() {

        PedidoCarga pedidoCarga = PedidoCarga.get(params.long('id'))
        if (!pedidoCarga) {
            flash.errors = "Pedido não encontrado"
            redirect(action: 'list')
            return;
        }

        String format = 'excel'
        String extention = 'xls'

        response.contentType = grailsApplication.config.grails.mime.types[format]
        response.setHeader("Content-disposition", "attachment; filename=PedidoCarga#${pedidoCarga.id}.${extention}")

        List fields = [
                "cpf",
                "matricula",
                "nome",
              //  "cartao",
                "dateCreated",
                "dataCarga",
                "status",
                "descricao",
                "valor"
        ]

        Map labels = [
                "cpf"        : "CPF",
                "matricula"  : "Matrícula",
                "nome"       : "Nome",
              //  "cartao"     : "Cartao",
                "dateCreated": "Data Pedido",
                "dataCarga"  : "Data Carga",
                "status"     : "Status Pedido",
                "descricao"  : "Lançamento",
                "valor"      : "Valor"
        ]

        Map formatters = [
                'valor': { domain, value -> return "R\$ ${Util.formatCurrency(value)}" },
        ]

        Map parameters = ["column.widths": [0.2, 0.2, 0.5, 0.2, 0.2]]

        def itemPedidoList = pedidoCarga.itens.collect {
            [
                    "cpf"      : it.participante.cpf,
                    "matricula": it.participante.matricula,
                    "nome"     : it.participante.nome,
                   // "cartao"   :  portador?.cartaoAtual?.numero,
                    "dateCreated"     : it.pedido.dateCreated,
                    "dataCarga"     : it.pedido.dataCarga,
                    "status"     : it.pedido.status,
                    "descricao": it.lancamento ? it.lancamento.tipo.nome : "CARGA",
                    "valor"    : it.valor



            ]
        }.sort { it.tipo?.nome }

        exportService.export(format, response.outputStream, itemPedidoList, fields, labels, formatters, parameters)
    }


    private def getTaxasACobrar(Unidade unidadeInstance) {
        return Lancamento.executeQuery(
                "select l from Lancamento l, Funcionario f " +
                        "where l.conta = f.conta and l.tipo in (:tipos) and l.status = :sts and f.unidade = :unid",

                [
                        unid : unidadeInstance,
                        tipos: [TipoLancamento.TAXA_UTILIZACAO, TipoLancamento.MENSALIDADE, TipoLancamento.EMISSAO_CARTAO, TipoLancamento.REEMISSAO_CARTAO],
                        sts  : StatusLancamento.A_FATURAR
                ]
        )
    }


    def loadTaxasCartao() {

        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        params.offset = params.offset ?: 0

        def unidadeInstance = Unidade.get(params.unidId)
        def pedidoInstance = PedidoCarga.get(params.pedId)

        if (unidadeInstance) {

            //Verifica se existem taxas a serem cobradas
            //Se existirem, adiciona como itens do pedido

            def lancList
            if (pedidoInstance) {

                lancList = Lancamento.executeQuery("""select l
from Lancamento l, ItemPedido i
where i.lancamento=l
and i.pedido=:ped
""", [
                        ped   : pedidoInstance,
                        max   : params.max,
                        offset: params.offset
                ])

            } else lancList = getTaxasACobrar(unidadeInstance, params)

            render(template: 'taxasList', model: [taxasList: lancList, taxasCount: lancList.size()])

        }
    }

    def liberarPedido() {
        def pedidoCargaInstance = PedidoCarga.get(params.id.toLong())
        if (pedidoCargaInstance) {
            def ret = pedidoCargaService.releasePedido(pedidoCargaInstance)
            if (ret.success)
                flash.success = ret.message
            else
                flash.error = ret.messagem
        } else
            flash.error = "Pedido não encontrado com ID: ${params.id}"

        redirect(action: 'list')
    }

    def cancelarPedido() {
        def pedidoCarga = PedidoCarga.get(params.id as long)
        if (pedidoCarga) {
            def ret = pedidoCargaService.cancelPedido(pedidoCarga)
            if (ret.success)
                flash.success = ret.message
            else
                flash.error = ret.message
        } else
            flash.error = "Pedido não encontrado com ID: $params.id"

        redirect(action: 'list')
    }
}
