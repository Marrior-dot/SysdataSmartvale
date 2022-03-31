package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Estabelecimento

class BaseEstabelecimentosService {


    def list(params, paginate = true) {

        def criteria = {

            if (params.nFanta) {
                eq("nomeFantasia", params.nFanta)
            }

            if (params.cnpj) {

                eq("cnpj", params.cnpj)
            }

            if (params.dataInicial) {
                gt('dateCreated', params.dataInicial)
            }
            if (params.dataFinal) {

                lt('dateCreated', params.dataFinal)
            }


        }


        if (paginate)
            return Estabelecimento.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Estabelecimento.createCriteria().list(criteria)

    }

    def count(pars) {

        def criteria = {

            if (pars.cnpj) {
                eq("cnpj", pars.cnpj)
            }
            if (pars.dataInicial) {
                gt('dateCreated', pars.dataInicial)
            }
            if (pars.dataFinal) {

                lt('dateCreated', pars.dataFinal)
            }

        }

        return Estabelecimento.createCriteria().count(criteria)

    }

    }
