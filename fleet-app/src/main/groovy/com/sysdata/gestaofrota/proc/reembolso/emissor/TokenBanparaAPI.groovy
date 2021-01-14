package com.sysdata.gestaofrota.proc.reembolso.emissor

import com.sysdata.gestaofrota.ChaveAcessoApi
import com.sysdata.gestaofrota.MensagemIntegracao
import com.sysdata.gestaofrota.StatusChaveAcesso
import com.sysdata.gestaofrota.TipoAplicacao
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.util.Holders
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

@Slf4j
trait TokenBanparaAPI {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    def withToken = { clos ->

        ChaveAcessoApi key = ChaveAcessoApi.findByStatusAndTipoAplicacao(StatusChaveAcesso.VALIDA,
                                                                            TipoAplicacao.CLIENTE_API_BANPARA)

        // Se não existe chave válida ou chave já expirou pelo tempo
        if (!key || key.dataHoraExpiracao < new Date() ) {

            def endpoint = Holders.grailsApplication.config.projeto.reembolso.banpara.api.autenticar.endpoint
            def credencial = [
                    usuario: Holders.grailsApplication.config.projeto.reembolso.banpara.api.autenticar.usuario,
                    chave: Holders.grailsApplication.config.projeto.reembolso.banpara.api.autenticar.chave
            ]

            MensagemIntegracao msgAutentica = new MensagemIntegracao(tipo: TipoMensagem.BANPARA_AUTENTICACAO)
            msgAutentica.corpo = (credencial as JSON).toString(true)

            ResponseData responseData = RESTClientHelper.instance.postJSON(endpoint, credencial)

            if (!responseData)
                throw new RuntimeException("Sem resposta do servidor!")

            msgAutentica.codigoResposta = responseData.statusCode.toString()
            msgAutentica.resposta = responseData.body
            msgAutentica.save(flush: true)

            if (responseData.statusCode == 200) {

                def receivedToken = responseData.json.token
                String receivedDateCreated = responseData.json.dataCriacao
                String receivedDateExpiration = responseData.json.dataExpiracao

                if (key) {
                    key.status = StatusChaveAcesso.EXPIRADA
                    key.save(flush: true)
                    log.info "Token #${key.id} expirada"
                }

                // Persiste novo token recuperado
                ChaveAcessoApi newKey = new ChaveAcessoApi(token: receivedToken)
                newKey.with {
                    dataHoraCriacao = dateFormat.parse(receivedDateCreated.replace("T", " "))
                    dataHoraExpiracao = dateFormat.parse(receivedDateExpiration.replace("T", " "))
                    tipoAplicacao = TipoAplicacao.CLIENTE_API_BANPARA
                }
                newKey.save(flush: true)
                log.info "Nova token #${newKey.id} recuperado"

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

}