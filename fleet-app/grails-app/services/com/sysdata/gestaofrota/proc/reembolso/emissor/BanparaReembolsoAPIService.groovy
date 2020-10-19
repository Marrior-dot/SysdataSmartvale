package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional


@Transactional
class BanparaReembolsoAPIService implements ExecutableProcessing {

    GrailsApplication grailsApplication

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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")


    private def withToken = { clos ->

        ChaveAcessoApi key = ChaveAcessoApi.findByStatusAndTipoAplicacao(StatusChaveAcesso.VALIDA,
                TipoAplicacao.CLIENTE_API_BANPARA)

        // Se não existe chave válida ou chave já expirou pelo tempo
        if (!key || key.dataHoraExpiracao < new Date() ) {

            def baseUrl = grailsApplication.config.projeto.reembolso.banpara.api.autenticar.baseUrl
            def endpoint = baseUrl + "/v1/autenticar"
            def credencial = [
                    usuario: grailsApplication.config.projeto.reembolso.banpara.api.autenticar.usuario,
                    chave: grailsApplication.config.projeto.reembolso.banpara.api.autenticar.chave
            ]

            MensagemIntegracao msgAutentica = new MensagemIntegracao(tipo: TipoMensagem.BANPARA_AUTENTICACAO)
            msgAutentica.corpo = (credencial as JSON).toString(true)

            ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, "/", credencial)

            msgAutentica.codigoResposta = responseData.statusCode
            msgAutentica.resposta = responseData.body
            msgAutentica.jsonResponse = responseData.json

            if (responseData.statusCode == 200) {

                def receivedToken = responseData.json.token
                String receivedDateCreated = responseData.json.dataCriacao
                String receivedDateExpiration = responseData.json.dataExpiracao

                if (key) {
                    key.status = StatusChaveAcesso.EXPIRADA
                    key.save(flush: true)
                }

                // Persiste novo token recuperado
                ChaveAcessoApi newKey = new ChaveAcessoApi()
                newKey.with {
                    token = receivedToken
                    dataHoraCriacao = dateFormat.parse(receivedDateCreated.replace("T", " "))
                    dataHoraExpiracao = dateFormat.parse(receivedDateExpiration.replace("T", " "))
                    tipoAplicacao = TipoAplicacao.CLIENTE_API_BANPARA
                }
                newKey.save(flush: true)

                clos(newKey.token)
            }

            else if (responseData.statusCode == 400) {

                log.error "Falha na autenticação: Erro de Negócio"

                responseData.json.each {
                    log.error "${it.key} = ${it.value}"
                }

            } else if (responseData.statusCode == 500)

                log.error "Falha na autenticação: Erro de Sistema"

            msgAutentica.save(flush: true)

            // Utiliza a chave previamente recuperada e armazenada
        } else
            clos(key.token)
    }

    private void aceitarLotePagamento(LotePagamento lotePagamento) {
        lotePagamento.status = StatusLotePagamento.ACEITO
        lotePagamento.pagamentos.each { PagamentoLote pgLt ->
            pgLt.status = StatusPagamentoLote.ACEITO
            pgLt.save(flush: true)
        }
    }

    private void rejeitarLotePagamento(LotePagamento lotePagamento, ResponseData responseData) {

        if (responseData.json) {

            StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByCodigo(responseData.json.codErro)
            if (!statusRetorno) {
                statusRetorno = new StatusRetornoPagamento(codigo: responseData.json.codErro, descricao: responseData.json.msgErro)
                statusRetorno.save(flush: true)
            } else if (statusRetorno && statusRetorno.descricao != responseData.json.msgErro ) {
                statusRetorno.descricao = responseData.json.msgErro
                statusRetorno.save(flush: true)
            }

            lotePagamento.statusRetorno = statusRetorno
        }

        lotePagamento.status = StatusLotePagamento.REJEITADO
        lotePagamento.save(flush: true)

        lotePagamento.pagamentos.each { PagamentoLote pgLt ->
            pgLt.status = StatusPagamentoLote.REJEITADO
            pgLt.save(flush: true)
        }

    }



    @Override
    def execute(Date date) {
        def pars = [sort: "dateCreated"]
        List<LotePagamento> lotePagtoList = LotePagamento.findAllByStatusEmissao(StatusEmissao.GERAR_ARQUIVO, pars)

        lotePagtoList.each { lote ->

            log.info "Preparando Lote de Pagamento $lote.id para envio a API Banpara..."

            def loteJson = [:]

            loteJson.Operador = grailsApplication.config.projeto.reembolso.api.transferencia.operador
            loteJson.dataContabil = lote.dateCreated.format('yyyy-MM-dd')
            loteJson.NSL = lote.id

            def pgtoOutrosBancosList = lote.pagamentos.findAll { it.dadoBancario.banco.codigo != "037" }

            def tedList = pgtoOutrosBancosList.collect { PagamentoLote pgtoLote ->

                [
                        NSR: pgtoLote.id,
                        AgenciaContaOrig: grailsApplication.config.projeto.reembolso.banpara.contaDebito.agencia,
                        ContaOrig: grailsApplication.config.projeto.reembolso.banpara.contaDebito.conta,
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
                        AgenciaContaOrig: grailsApplication.config.projeto.reembolso.banpara.contaDebito.agencia,
                        ContaOrig: grailsApplication.config.projeto.reembolso.banpara.contaDebito.conta,
                        ProdutoDest: 1,
                        AgenciaContaDest: pgtoLote.dadoBancario.agencia,
                        ContaDest: pgtoLote.dadoBancario.conta,
                        Valor: pgtoLote.valor
                ]
            }

            loteJson.TEF = tefList

            withToken { token ->
                MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
                msgEnviaLote.with {
                    tipo = TipoMensagem.BANPARA_ENVIO_LOTEPAGAMENTO
                    corpo = (loteJson as JSON).toString(true)
                }
                msgEnviaLote.save(flush: true)

                def baseUrl = grailsApplication.config.projeto.reembolso.banpara.api.transferencia.baseUrl
                def endpoint = baseUrl + "/contacorrente/v1/lote"

                def header = [:]
                if (token)
                    header.Authorization = "Bearer $token"

                ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, loteJson, header)

                msgEnviaLote.codigoResposta = responseData.statusCode
                msgEnviaLote.resposta = responseData.body
                msgEnviaLote.jsonResponse = responseData.json

                lote.statusEmissao = StatusEmissao.ARQUIVO_GERADO

                switch (responseData.statusCode) {

                    case 200:
                        aceitarLotePagamento(lote)
                        break

                    case 400:
                        rejeitarLotePagamento(lote, responseData)
                        break

                    case 500:
                        rejeitarLotePagamento(lote, responseData)
                        break

                    default:
                        throw new RuntimeException("Codigo Retorno (responseData.statusCode) não tratado")
                }
            }
        }
    }
}

import grails.util.Holders

import java.text.SimpleDateFormat