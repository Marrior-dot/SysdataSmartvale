package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.Veiculo
import grails.core.GrailsApplication

class BaseFuncionariosRelatorioController {

    def exportService

    GrailsApplication grailsApplication


    BaseFuncionariosService baseFuncionariosService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 1


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseFuncionarios.${params.extension}")

            def cabecalhoBaseFuncionarioRelatorio = []
            def cabecalho = [:]
            cabecalho.cliente = "EMISSAO"
            cabecalho.unidade = new Date().format('dd/MM/yyyy')
            cabecalhoBaseFuncionarioRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.empresa) {
                cabecalho1.cliente = "EMPRESA"
                Rh empresaCliente = Rh.get(params.empresa.toLong())
                cabecalho1.unidade = empresaCliente.nomeFantasia
                cabecalhoBaseFuncionarioRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            if (params.unidade) {
                cabecalho2.cliente = "UNIDADE"
                Unidade unidade = Unidade.get(params.unidade.toLong())
                cabecalho2.unidade = unidade.nome
                cabecalhoBaseFuncionarioRelatorio << cabecalho2
            }
            def cabecalho3 = [:]
            cabecalho3.cliente = ""
            cabecalhoBaseFuncionarioRelatorio << cabecalho3

            def cabecalho4 = [:]
            cabecalho4.cliente = "CLIENTE"
            cabecalho4.unidade = "UNIDADE"
            cabecalho4.matricula = "MATRICULA"
            cabecalho4.nome = "NOME"
            cabecalho4.cpf = "CPF"
            cabecalho4.cartao = "CARTAO"
            cabecalho4.cnh = "CNH"
            cabecalho4.cnhCategoria = "CNH CATEGORIA"
            cabecalho4.cnhValidade = "CNH VALIDADE"
            cabecalho4.email = "EMAIL"
            cabecalho4.telefone = "TELEFONE"
            cabecalho4.endereco = "ENDERECO"
            cabecalho4.complemento = "COMPLEMENTO"
            cabecalho4.bairro = "BAIRRO"
            cabecalho4.cidade = "CIDADE"
            cabecalho4.estado = "ESTADO"
            cabecalho4.cep = "CEP"
            cabecalhoBaseFuncionarioRelatorio << cabecalho4

            def reportList = baseFuncionariosService.list(params, false)

            //def totalValor = reportList.sum { it.valor }

            reportList = reportList.collect { tr ->
                [
                        "cliente": tr.unidade.rh.nome,
                        "unidade": tr.unidade.nome,
                        "matricula": tr.matricula,
                        "nome": tr.nome,
                        "cpf": tr.cpf,
                        "cartao": tr?.portador?.cartaoAtual?.numero,
                        "cnh": tr.cnh,
                        "cnhCategoria": tr.categoriaCnh.nome,
                        "cnhValidade" : tr.validadeCnh.format('dd/MM/yy HH:mm:ss'),
                        "cnh": tr.cnh,
                        "cnhCategoria": tr.categoriaCnh.nome,
                        "cnhValidade" : tr.validadeCnh.format('dd/MM/yy HH:mm:ss'),
                        "email": tr.email,
                        "telefone": tr.telefone,
                        "endereco": "${tr?.endereco?.logradouro} ${tr?.portador?.endereco?.numero}",
                        "complemento": tr.endereco.complemento,
                        "bairro": tr.endereco.bairro,
                        "cidade": tr.endereco.cidade.nome,
                        "estado": tr.endereco.cidade.estado,
                        "cep": tr.endereco.cep
                        /*"cliente": tr.unidade.rh.nome,
                        "unidade": tr.unidade.nome,
                        "matricula": tr.matricula,
                        "nome": tr.nome,
                        "cpf": tr.cpf,
                        "cartao": tr.portador.cartaoAtual.numero,
                        "cnh": tr.cnh,
                        "cnhCategoria": tr.categoriaCnh.nome,
                        "cnhValidade" : tr.validadeCnh.format('dd/MM/yy HH:mm:ss'),
                        "email": tr.email,
                        "telefone": tr.telefone,
                        "endereco": "${tr.endereco.logradouro} - ${tr.portador.endereco.numero}",
                        "complemento": tr.endereco.complemento,
                        "bairro": tr.endereco.bairro,
                        "cidade": tr.endereco.cidade.nome,
                        "estado": tr.endereco.cidade.estado,
                        "cep": tr.endereco.cep*/
                ]
            }
            reportList += [
                    "cliente": "",
                    "unidade": "",
                    "matricula": "",
                    "nome": "",
                    "cpf": "",
                    "cartao": "",
                    "cnh": "",
                    "cnhCategoria": "",
                    "cnhValidade" : "",
                    "email": "",
                    "telefone": "",
                    "endereco": "",
                    "complemento": "",
                    "bairro": "",
                    "cidade": "",
                    "estado": "",
                    "cep": ""
                    ]
            List fields = [
                    "cliente",
                    "unidade",
                    "matricula",
                    "nome",
                    "cpf",
                    "cartao",
                    "cnh",
                    "cnhCategoria",
                    "cnhValidade",
                    "email",
                    "telefone",
                    "endereco",
                    "complemento",
                    "bairro",
                    "cidade",
                    "estado",
                    "cep"
            ]

            Map labels = [
                    "cliente": "CLIENTE",
                    "unidade": "UNIDADE",
                    "matricula": "MATRICULA",
                    "nome": "NOME",
                    "cpf": "CPF",
                    "cartao": "CARTAO",
                    "cnh": "CNH",
                    "cnhCategoria": "CNH CATEGORIA",
                    "cnhValidade" : "CNH VALIDADE",
                    "email": "EMAIL",
                    "telefone": "TELEFONE",
                    "endereco": "ENDERECO",
                    "complemento": "COMPLEMENTO",
                    "bairro": "BAIRRO",
                    "cidade": "CIDADE",
                    "estado": "ESTADO",
                    "cep": "CEP"
            ]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Funcionarios", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                    cabecalhoBaseFuncionarioRelatorio+reportList,
                                fields,
                                labels, [:], ['header.enabled': false])
            return 
        }

        [baseFuncionariosList: baseFuncionariosService.list(params), baseFuncionariosCount: baseFuncionariosService.count(params)]
    }


    }

