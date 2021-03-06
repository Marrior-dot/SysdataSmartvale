package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Rh
import grails.core.GrailsApplication

class BaseEstabelecimentosRelatorioController {

    def exportService

    GrailsApplication grailsApplication

    BaseEstabelecimentosService  baseEstabelecimentosService


    def index() {

        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 1


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseEstabelecimentos.${params.extension}")

            def BaseEstabelecimentosRelatorio = []

            def cabecalho = [:]
            cabecalho.codigo = "EMISSAO"
            cabecalho.cnpj = new Date().format('dd/MM/yyyy')
            BaseEstabelecimentosRelatorio << cabecalho

            def cabecalho1 = [:]
            if (params.nFanta) {
                cabecalho1.codigo = "EMPRESA"
                cabecalho1.cnpj = params.nFanta
                BaseEstabelecimentosRelatorio << cabecalho1
            }
            def cabecalho2 = [:]
            cabecalho2.codigo = ""
            BaseEstabelecimentosRelatorio << cabecalho2

            def cabecalho3 = [:]
            cabecalho3.codigo = "CODIGO"
            cabecalho3.cnpj = "CNPJ"
            cabecalho3.nome = "NOME"
            cabecalho3.nomeFantasia = "NOME FANTASIA"
            cabecalho3.Numero = "NUMERO"
            cabecalho3.Logradouro = "LOGRADOURO"
            cabecalho3.Complemento = "COMPLEMENTO"
            cabecalho3.Bairro = "BAIRRO"
            cabecalho3.Cidade = "CIDADE"
            cabecalho3.Estado = "ESTADO"
            cabecalho3.Cep = "CEP"
            cabecalho3.Email = "EMAIL"
            BaseEstabelecimentosRelatorio << cabecalho3

            def reportList = baseEstabelecimentosService.list(params, true)

            //def totalValor = reportList.sum { it.valor }

            reportList = reportList.collect { tr ->
                [
                        "codigo": tr.codigo,
                        "cnpj": tr.cnpj,
                        "nome": tr.nome,
                        "nomeFantasia": tr.nomeFantasia,
                        "Numero": tr.telefone,
                        "Logradouro": tr.empresa.endereco.logradouro,
                        "Complemento": tr.empresa.endereco.complemento,
                        "Bairro": tr.empresa.endereco.bairro,
                        "Cidade": tr.endereco.cidade.nome,
                        "Estado": tr.empresa.endereco.cidade.estado,
                        "Cep": tr.empresa.endereco.cep,
                        "Email": tr.empresa.email,
                        //"Banco": tr.empresa.dadoBancario.banco.nome,
                        //"TipoTitular": tr.empresa.dadoBancario.tipoTitular
                ]
            }

            reportList += [
                    "codigo": "",
                    "cnpj": "",
                    "nome": "",
                    "nomeFantasia": "",
                     "Numero": "",
                    "Logradouro": "",
                    "Complemento": "",
                    "Bairro": "",
                    "Cidade": "",
                    "Estado": "",
                    "Cep": "",
                    "Email": "",
                    //"Banco": "",
                    //"TipoTitular": ""
            ]

            List fields = [
                    "codigo",
                    "cnpj",
                    "nome",
                    "nomeFantasia",
                    "Numero",
                    "Logradouro",
                    "Complemento",
                    "Bairro",
                    "Cidade",
                    "Estado",
                    "Cep",
                    "Email",
                    //"Banco",
                   // "TipoTitular"
                            /*"codigo",
                            "cnpj",
                            "nome",
                            "nomeFantasia",
                            //"telefone",
                            "empresa.endereco.logradouro",
                            "empresa.endereco.numero",
                            "empresa.endereco.complemento",
                            "empresa.endereco.bairro",
                            "empresa.endereco.cidade.nome",
                            "empresa.endereco.cidade.estado",
                            "empresa.endereco.cep", "email",
                            //"empresa.taxaReembolso",
                            "empresa.dadoBancario.banco.nome",
                            //"empresa.dadoBancario.agencia", "empresa.dadoBancario.conta",
                            "empresa.dadoBancario.tipoTitular"
                            //"empresa.dadoBancario.nomeTitular","empresa.dadoBancario.documentoTitular"*/
                          ]

            Map labels = [
                    "codigo": "CODIGO",
                    "cnpj": "CNPJ",
                    "nome": "RAZAO SOCIAL",
                    "nomeFantasia": "NOME FANTASIA",
                    "Numero": "NUMERO",
                    "Logradouro": "LOGRADOURO",
                    "Complemento": "COMPLEMENTO",
                    "Bairro": "BAIRRO",
                    "Cidade": "CIDADE",
                    "Estado": "UF",
                    "Cep": "CEP",
                    "Email": "EMAIL",
                    //"Banco": "BAIRRO",
                    //"TipoTitular": "TIPO TITULAR"
                    /*"codigo": "Cod.Estab",
                          "cnpj": "CNPJ",
                          "nome": "Raz??o Social",
                          "nomeFantasia": "Fantasia",
                          //"telefone": "Telefone",
                          "empresa.endereco.logradouro": "Logradouro",
                          "empresa.endereco.numero": "Numero",
                          "empresa.endereco.complemento": "Complemento",
                          "empresa.endereco.bairro": "Bairro",
                          "empresa.endereco.cidade.nome": "Cidade",
                          "empresa.endereco.cidade.estado": "Estado",
                          "empresa.endereco.cep": "Cep",
                          "email": "Email",
                          //"empresa.taxaReembolso": "Taxa Reembolso(%)",
                          "empresa.dadoBancario.banco.nome": "Banco",
                          //"empresa.dadoBancario.agencia": "Agencia","empresa.dadoBancario.conta": "Conta",
                          "empresa.dadoBancario.tipoTitular": "Tipo Titular"
                          //"empresa.dadoBancario.nomeTitular": "Nome Titular", "empresa.dadoBancario.documentoTitular": "Documento"*/
                          ]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Estabelecimentos", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                    BaseEstabelecimentosRelatorio+reportList,
                                fields,
                                labels, [:], ['header.enabled': false])

            return

        }

        [baseEstabelecimentosList: baseEstabelecimentosService.list(params), baseEstabelecimentosCount: baseEstabelecimentosService.count(params)]
    }


    }

