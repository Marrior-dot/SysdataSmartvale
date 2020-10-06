package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.LotePagamento
import com.sysdata.gestaofrota.MensagemIntegracao
import com.sysdata.gestaofrota.PagamentoLote
import com.sysdata.gestaofrota.StatusEmissao
import com.sysdata.gestaofrota.StatusLotePagamento
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class BanparaReembolsoAPIService implements ExecutableProcessing {


/*
    {
        "Operador": "3157",
        "dataContabil": "2020-08-26",
        "NSL": 1,
        "TED": [
            {
                "NSR": 1,
                "AgenciaContaOrig": 15,
                "ContaOrig": 213456,
                "BancoDest": 3,
                "ProdutoDest": 1,
                "AgenciaContaDest": 103,
                "ContaDest": 123456,
                "NomeDest": "CLIENTE TESTE",
                "CPFCNPJDest": 191,
                "TipoPessoaDest": "F",
                "Finalidade": 10,
                "Historico": "",
                "IdTransf": "",
                "Valor": 100
            }
        ],
        "TEF": [
            {
                "NSR": 2,
                "AgenciaContaOrig": 15,
                "ContaOrig": 213456,
                "ProdutoDest": 1,
                "AgenciaContaDest": 103,
                "ContaDest": 123456,
                "Valor": 100
            }
        ]
    }

*/

    GrailsApplication grailsApplication

    private Closure withToken = { clos ->

        def baseUrl = grailsApplication.config.project.reembolso.banpara.api.autenticar.baseUrl
        def endpoint = baseUrl + "/v1/autenticar"
        def credencial = [usuario: grailsApplication.config.project.reembolso.banpara.api.autenticar.usuario]

        MensagemIntegracao msgAutentica = new MensagemIntegracao(tipo: TipoMensagem.BANPARA_AUTENTICACAO)
        msgAutentica.corpo = (credencial as JSON).toString(true)

        ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, "/", credencial)

        msgAutentica.codigoResposta = responseData.statusCode

        if (responseData.statusCode == 200)
            clos()
        else if (responseData.statusCode == 400) {
            log.error "Falha na autenticação: Erro de Negócio"

            msgAutentica.resposta = responseData.body

            responseData.json.each {
                log.error "${it.key} = ${it.value}"
            }

        } else if (responseData.statusCode == 500)
            log.error "Falha na autenticação: Erro de Sistema"

        msgAutentica.save()

    }



    @Override
    def execute(Date date) {
        def pars = [sort: "dateCreated"]
        List<LotePagamento> lotePagtoList = LotePagamento.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, pars)

        lotePagtoList.each { lote ->

            log.info "Preparando Lote de Pagamento $lote.id para envio a API Banpara..."

            def loteJson = [:]

            loteJson.Operador = grailsApplication.config.project.reembolso.api.transferencia.operador
            loteJson.dataContabil = lote.dateCreated.format('yyyy-MM-dd')
            loteJson.NSL = lote.id

            def pgtoOutrosBancosList = lote.pagamentos.findAll { it.dadoBancario.banco.codigo != "037" }

            def tedList = pgtoOutrosBancosList.collect { PagamentoLote pgtoLote ->

                            [
                                NSR: pgtoLote.id,
                                AgenciaContaOrig: grailsApplication.config.project.reembolso.banpara.contaDebito.agencia,
                                ContaOrig: grailsApplication.config.project.reembolso.banpara.contaDebito.conta,
                                BancoDest: pgtoLote.dadoBancario.banco.codigo,
                                AgenciaContaDest: pgtoLote.dadoBancario.agencia,
                                ContaDest: pgtoLote.dadoBancario.conta,
                                ProdutoDest: 1,
                                NomeDest: pgtoLote.estabelecimento.nomeFantasia,
                                CPFCNPJDest: pgtoLote.estabelecimento.cnpj,
                                TipoPessoaDest: "J",
                                Finalidade: 10,
                                Historico: "",
                                IdTransf: "",
                                Valor: pgtoLote.valor
                            ]
            }

            loteJson.TED = tedList

            def pgtoBanparaList = lote.pagamentos.findAll { it.dadoBancario.banco.codigo == "037" }

            def tefList = pgtoBanparaList.collect { PagamentoLote pgtoLote ->
                            [
                                NSR: pgtoLote.id,
                                AgenciaContaOrig: grailsApplication.config.project.reembolso.banpara.contaDebito.agencia,
                                ContaOrig: grailsApplication.config.project.reembolso.banpara.contaDebito.conta,
                                ProdutoDest: 1,
                                AgenciaContaDest: pgtoLote.dadoBancario.agencia,
                                ContaDest: pgtoLote.dadoBancario.conta,
                                Valor: pgtoLote.valor
                            ]
            }

            loteJson.TEF = tefList

            withToken {
                MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
                msgEnviaLote.with {
                    tipo = TipoMensagem.BANPARA_ENVIO_LOTEPAGAMENTO
                    corpo = (loteJson as JSON).toString(true)
                }
                msgEnviaLote.save()

                def baseUrl = grailsApplication.config.project.reembolso.api.transferencia.baseUrl
                def endpoint = baseUrl + "//contacorrente/v1/lote"
                ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, loteJson)

                msgEnviaLote.resposta = responseData.body

                lote.statusEmissao = StatusEmissao.ARQUIVO_GERADO
                if (responseData.statusCode == 200) {
                    lote.status = StatusLotePagamento.ACEITO
                } else if (responseData.statusCode == 400) {
                    lote.status = StatusLotePagamento.REJEITADO

                    responseData.json


                } else if (responseData.statusCode == 500) {
                    lote.status = StatusLotePagamento.REJEITADO

                }
                lote.save()
            }


        }
    }
}



