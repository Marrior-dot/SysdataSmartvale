package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Estabelecimento
import com.sysdata.gestaofrota.Empresa

import grails.core.GrailsApplication

class BaseEstabelecimentosController {

    def exportService

    GrailsApplication grailsApplication

    BaseEstabelecimentosService  baseEstabelecimentosService


    def index() {


        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0


        if (params?.f && params.f != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.f]
            response.setHeader("Content-disposition", "attachment; filename=baseEstabelecimentos.${params.extension}")



            List fields = ["codigo", "cnpj", "nome","nomeFantasia", "telefone", "empresa.endereco.logradouro","empresa.endereco.numero",
                           "empresa.endereco.complemento", "empresa.endereco.bairro","empresa.endereco.cidade.nome","empresa.endereco.cidade.estado",
                           "empresa.endereco.cep", "email", "empresa.taxaReembolso", "empresa.dadoBancario.banco.nome", "empresa.dadoBancario.agencia",
                           "empresa.dadoBancario.conta",  "empresa.dadoBancario.tipoTitular", "empresa.dadoBancario.nomeTitular",
                           "empresa.dadoBancario.documentoTitular"]

            Map labels = ["codigo": "Cod.Estab", "cnpj": "CNPJ", "nome": "Razão Social", "nomeFantasia": "Fantasia",
                          "telefone": "Telefone", "empresa.endereco.logradouro": "Logradouro","empresa.endereco.numero": "Numero",
                          "empresa.endereco.complemento": "Complemento", "empresa.endereco.bairro": "Bairro",
                          "empresa.endereco.cidade.nome": "Cidade", "empresa.endereco.cidade.estado": "Estado",
                          "empresa.endereco.cep": "Cep","email": "Email", "empresa.taxaReembolso": "Taxa Reembolso(%)",
                          "empresa.dadoBancario.banco.nome": "Banco", "empresa.dadoBancario.agencia": "Agencia",
                          "empresa.dadoBancario.conta": "Conta", "empresa.dadoBancario.tipoTitular": "Tipo Titular",
                          "empresa.dadoBancario.nomeTitular": "Nome Titular", "empresa.dadoBancario.documentoTitular": "Documento" ]


            //  Map formatters = [author: upperCase]
            Map parameters = [title: "Base de Estabelecimentos", "column.widths": [0.2, 0.3, 0.5]]

            exportService.export(params.f,
                                response.outputStream,
                                baseEstabelecimentosService.list(params, false),
                                fields,
                                labels, [:], [:])

            return

        }

        [baseEstabelecimentosList: baseEstabelecimentosService.list(params), baseEstabelecimentosCount: baseEstabelecimentosService.count(params)]
    }


    }

