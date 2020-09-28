package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Estabelecimento

class BaseEstabelecimentosService {


    def list(params, paginate = true) {

        def criteria = {

            if (params.empresa)
                unidade {
                    rh {
                        eq("id", params.empresa.toLong())
                    }
                }
            if (params.unidade) {

                unidade {
                    eq("id", params.unidade.toLong())
                }
            }


        }


        if (paginate)
            return Estabelecimento.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Estabelecimento.createCriteria().list(criteria)

    }

    def count(pars) {

        def criteria = {

            if (pars.empresa)
                unidade {
                    rh {
                        eq("id", pars.empresa.toLong())
                    }
                }
            if (pars.unidade) {

                unidade {
                    eq("id", pars.unidade.toLong())
                }
            }

        }

        return Estabelecimento.createCriteria().count(criteria)

    }


    }
