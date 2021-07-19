package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.http.RESTClientHelper
import com.sysdata.gestaofrota.http.ResponseData
import grails.converters.JSON
import grails.gorm.transactions.Transactional

@Transactional
class EnderecoService {

    def findEnderecoByCep(cep) {

        RESTClientHelper helper = new RESTClientHelper()
        ResponseData resp = helper.getJSON("http://viacep.com.br/ws/$cep/json/", "/" )
        if (resp.statusCode == 200) {
            def jsonEnd = resp.json
            def end = [:]
            if (jsonEnd) {
                Estado estado = Estado.findByUf(jsonEnd.uf)
                if (! estado) {
                    log.warn ("Estado $jsonEnd.uf não localizado no banco de dados")
                    return end as JSON

                }

                Cidade cidade = Cidade.withCriteria(uniqueResult: true) {
                                    eq("estado", estado)
                                    ilike("nome", jsonEnd.localidade + "%")
                                }
                if (!cidade) {
                    cidade = new Cidade(nome: jsonEnd.localidade, estado: estado)
                    cidade.save(flush: true)
                    log.info "(+) Cidade: $cidade"
                }
                end['logradouro'] = jsonEnd.logradouro
                end['complemento'] = jsonEnd.complemento
                end['bairro'] = jsonEnd.bairro
                end['cidadeId'] = cidade.id
                end['estadoId'] = estado.id
                return end as JSON
            }
        }

    }
}
