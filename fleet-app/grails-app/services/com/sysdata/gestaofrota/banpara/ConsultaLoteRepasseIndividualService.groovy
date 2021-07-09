package com.sysdata.gestaofrota.banpara

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import com.sysdata.gestaofrota.proc.reembolso.emissor.TokenBanparaAPI
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class ConsultaLoteRepasseIndividualService implements TokenBanparaAPI {

    GrailsApplication grailsApplication

    private tratarPagamentoLote(PagamentoLote pgtoLote, tefTed) {
        switch (tefTed.status) {
            case 1:
                log.info "PG #${pgtoLote.id} Pendente"
                break

            case 2:
                // Liquidado somente quando TEF - conta destino BANPARÁ
                if (pgtoLote.dadoBancario.banco.codigo == "37") {
                    log.info "Conta Banpara:"
                    liquidarPagamento(pgtoLote)
                } else
                    log.info "PG #${pgtoLote.id} Confirmado - (conta não Banpará)"

                break

            case [3, 4]:
                pgtoLote.status = StatusPagamentoLote.REJEITADO
                StatusRetornoPagamento statusRetorno = StatusRetornoPagamento.findByDescricao(tefTed.mensagem)
                if (! statusRetorno) {
                    Integer novoCodigo = StatusRetornoPagamento.findNextCodigo()
                    statusRetorno = new StatusRetornoPagamento(codigo: novoCodigo, descricao: tefTed.mensagem)
                    statusRetorno.save(flush: true)
                }
                pgtoLote.statusRetorno = statusRetorno
                pgtoLote.save(flush: true)
                log.info "PG #${pgtoLote.id} Rejeitado. Status: ${tefTed.status}"
                break

            default:
                throw new RuntimeException("Erro não mapeado: ${tefTed.status}!")

        }
    }

    private void tratarResposta(PagamentoLote pagamentoLote, ResponseData response) {

        if (response.json) {

            def ted = response.json.ted.find { it.nsr == pagamentoLote.id }
            def tef = response.json.tef.find { it.nsr == pagamentoLote.id }


            tratarPagamentoLote(pagamentoLote, )
        }
    }


    def queryPayment(PagamentoLote pagamentoLote) {

        ResponseData responseData

        withToken { token ->

            def query = [:]
            query.Operador = grailsApplication.config.projeto.reembolso.banpara.api.lote.operador
            query.dataContabil = pagamentoLote.lotePagamento.dataEfetivacao.format('yyyy-MM-dd')
            query.NSL = pagamentoLote.lotePagamento.id
            query.NSR = pagamentoLote.id

            MensagemIntegracao msgEnviaLote = new MensagemIntegracao()
            msgEnviaLote.with {
                tipo = TipoMensagem.BANPARA_CONSULTA_LOTEPAGAMENTO
                corpo = query.toString()
            }
            msgEnviaLote.save(flush: true)

            def endpoint = grailsApplication.config.projeto.reembolso.banpara.api.lote.endpoint

            def header = [:]
            if (token)
                header.Authorization = "Bearer $token"

            responseData = RESTClientHelper.instance.get(endpoint, query, header)
            if (!responseData)
                throw new RuntimeException("Sem resposta do servidor!")

            msgEnviaLote.codigoResposta = responseData.statusCode.toString()
            msgEnviaLote.resposta = responseData.body
            msgEnviaLote.save(flush: true)

/*
            if (responseData.statusCode == 200)
                tratarResposta(pagamentoLote, responseData)

            else
                log.error "Erro ao Consultar NSL #${pagamentoLote.lotePagamento.id} | NSR #${pagamentoLote.id}. HTTP Status: ${responseData.statusCode}"
*/
        }

        return responseData
    }
}
