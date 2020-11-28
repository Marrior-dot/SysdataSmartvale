package com.sysdata.commons

import com.sysdata.gestaofrota.MensagemIntegracao
import com.sysdata.gestaofrota.TipoMensagem
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class IntegrationMessengerService {

/*
    User usuario
    String corpo
    String resposta
    String codigoResposta
    Map jsonResponse
    TipoMensagem tipo
*/

    def postAsJSON(String endpoint, TipoMensagem tipoMensagem, Map dados) {

        MensagemIntegracao mensagemIntegracao = new MensagemIntegracao(tipo: tipoMensagem, corpo: (dados as JSON).toString(true))
        mensagemIntegracao.save(flush: true)

        ResponseData response = RESTClientHelper.instance.postJSON(endpoint, dados)

        if (response) {
            mensagemIntegracao.codigoResposta = response.statusCode.toString()
            mensagemIntegracao.resposta = response.body
        }
        mensagemIntegracao.save(flush: true)

        return response
    }
}
