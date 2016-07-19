package com.sysdata.gestaofrota

import grails.converters.JSON
import grails.plugins.springsecurity.Secured


@Secured(['IS_AUTHENTICATED_FULLY'])
class PedidoCargaController extends BaseOwnerController {
    def exportService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        Unidade unidadeInstance = getUnidade()

        def criteria = {
            if (params?.searchDataCarga) {
                Date beginDay = Date.parse('dd/MM/yyyy', params.searchDataCarga.toString()).clearTime()
                gt('dataCarga', beginDay)
                lt('dataCarga', beginDay + 1)
            }

            if (params?.searchDataPedido) {
                Date beginDay = Date.parse('dd/MM/yyyy', params.searchDataPedido.toString()).clearTime()
                gt('dateCreated', beginDay)
                lt('dateCreated', beginDay + 1)
            }

            unidade {
                if (params?.searchUnidade) {
                    ilike('nome', "%${params.searchUnidade}%")
                }

                if (unidadeInstance) {
                    idEq(unidadeInstance.id)
                }
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
         unidadeInstance         : unidadeInstance,
         statusPedidoCarga       : StatusPedidoCarga.asList(),
         searchDataPedido        : params?.searchDataPedido,
         searchDataCarga         : params?.searchDataCarga,
         searchUnidade           : params?.searchUnidade,
         searchStatus            : params?.searchStatus]
    }

    def create() {
        def unidadeInstance = Unidade.get(params.long('unidade_id'))

        if (!unidadeInstance) {
            flash.errors = "Unidade não selecionada!"
            redirect(action: 'list')
            return;
        }

        PedidoCarga pedidoCargaInstance = new PedidoCarga()
        pedidoCargaInstance.unidade = unidadeInstance
        pedidoCargaInstance.dataCarga = new Date().clearTime()

        [pedidoCargaInstance: pedidoCargaInstance]
    }

    def save = {
        Unidade unidadeInstance = Unidade.get(params.long('unidade_id'))
        if (!unidadeInstance) {
            flash.errors = "Unidade não selecionada"
            redirect(action: 'list')
            return;
        }
        Date dataCarga = Date.parse("dd/MM/yyyy", params.dataCarga.toString())
        if (!dataCarga) {
            flash.errors = "Data de carga não definida"
            redirect(action: 'create', params: [unidade_id: unidadeInstance.id])
            return;
        }
        if (dataCarga < new Date() - 1) {
            flash.errors = "Data não pode ser inferior a hoje"
            redirect(action: 'create', params: [unidade_id: unidadeInstance.id])
            return;
        }
        def funcionariosInativosIds = params.list('funcionariosInativos')?.collect { it as Long }
        def funcionarios = Funcionario.findAllByCategoriaInList(unidadeInstance?.rh?.categoriasFuncionario)

        PedidoCarga pedidoCarga = new PedidoCarga()
        pedidoCarga.usuario = springSecurityService.getCurrentUser() as User
        pedidoCarga.dataCarga = dataCarga.clearTime()
        pedidoCarga.unidade = unidadeInstance
        pedidoCarga.validade = 180
        pedidoCarga.taxa = unidadeInstance?.rh?.taxaPedido
        pedidoCarga.total = 0D

        funcionarios.each { funcionario ->
            ItemPedido itemPedido = new ItemPedido()
            itemPedido.participante = funcionario
            itemPedido.valor = params?.double("valorCarga[${funcionario.id}]") ?: funcionario?.categoria?.valorCarga
            itemPedido.sobra = 0.0D
            itemPedido.ativo = !funcionariosInativosIds.contains(funcionario.id)
            itemPedido.save(flush: true)

            pedidoCarga.addToItens(itemPedido)
            if (itemPedido.ativo) pedidoCarga.total += itemPedido.valor
        }

        pedidoCarga.status = StatusPedidoCarga.NOVO
        Double taxa = pedidoCarga?.unidade?.rh?.taxaPedido ?: 0.0D
        pedidoCarga.total += taxa / 100 * pedidoCarga.total
        if (!pedidoCarga.save(flush: true)) {
            flash.errors = pedidoCarga.errors
            redirect(action: 'create', params: [unidade_id: unidadeInstance.id])
            return;
        } else flash.message = "Pedido criado com sucesso!"

        redirect(action: 'list')
    }

    def show = {
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        if (!pedidoCargaInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
            return;
        }


        [pedidoCargaInstance: pedidoCargaInstance]
    }

    def edit = {
        def pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        if (!pedidoCargaInstance) {
            flash.errors = "${message(code: 'default.not.found.message', args: [message(code: 'pedidoCarga.label', default: 'PedidoCarga'), params.id])}"
            redirect(action: "list")
        }

        [pedidoCargaInstance: pedidoCargaInstance]
    }

    def update = {
//        println("params: ${params}")

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
                pedidoCargaInstance.delete(flush: true)
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

    def filterFuncionarios = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        CategoriaFuncionario categoriaInstance = CategoriaFuncionario.get(params.long('categoria'))

        def criteria = {
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

            if (pedidoCargaInstance?.itens) {
                'in'('id', pedidoCargaInstance.itens*.participante.id)
            }

            order('nome')
        }

        def coutCriteria = {
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

            if (pedidoCargaInstance?.itens) {
                'in'('id', pedidoCargaInstance.itens*.participante.id)
            }
        }

        render(template: '/pedidoCarga/funcionarioList',
                model: [pedidoCargaInstance     : pedidoCargaInstance,
                        funcionarioInstanceList : Funcionario.createCriteria().list(params, criteria),
                        funcionarioInstanceCount: Funcionario.createCriteria().count(coutCriteria),
                        categoriaInstance       : categoriaInstance,
                        action                  : params.actionView])
    }

    def getAllFuncionariosIds = {
        PedidoCarga pedidoCargaInstance = PedidoCarga.get(params.long('id'))
        CategoriaFuncionario categoriaInstance = CategoriaFuncionario.get(params.long('categoria'))

        def funcionarioInstanceList = Funcionario.createCriteria().list {
            if (categoriaInstance) {
                eq('categoria', categoriaInstance)
            }

            if (pedidoCargaInstance?.itens) {
                'in'('id', pedidoCargaInstance.itens*.participante.id)
            }

            order('nome')

            projections {
                property('id')
            }
        }

        def model = [idsFuncionarios: funcionarioInstanceList, valorCategoria: Util.toBigDecial(categoriaInstance?.valorCarga).toString()]
        render model as JSON
    }

    def gerarPlanilha = {
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
                "ativo",
                "participante.nome",
                "valor",
                "participante.cpf",
                "participante.matricula",
                "participante.rg",
                "participante.dataNascimento",
                "participante.categoria.nome"
        ]
        Map labels = [
                "ativo"                      : "Ativo",
                "participante.nome"          : "Nome",
                "valor"                      : "Valor",
                "participante.cpf"           : "CPF",
                "participante.matricula"     : "Matrícula",
                "participante.rg"            : "RG",
                "participante.dataNascimento": "Dt. Nascimento",
                "participante.categoria.nome": "Categoria"
        ]

        Map formatters = [
                'ativo'                      : { domain, value -> return value ? "Ativado" : "Desativado" },
                'valor'                      : { domain, value -> return "R\$ ${Util.formatCurrency(value)}" },
                'participante.dataNascimento': { domain, value -> return value.format("dd/MM/yyyy") }
        ]

        Map parameters = ["column.widths": [0.1, 0.5, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2]]


        List<ItemPedido> itemPedidoList = pedidoCarga.itens.collect { it as ItemPedido }?.sort { it.participante.nome }
        exportService.export(format, response.outputStream, itemPedidoList, fields, labels, formatters, parameters)
    }
}
