package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.ItemPedido
import com.sysdata.gestaofrota.Funcionario

class ControleMensalCargasService {

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

            if (params.dataInicial){

               gt('dateCreated', params.dataInicial)
            }

            if (params.dataFinal) {

                lt('dateCreated', params.dataFinal)
            }
        }

        if (paginate)
            return ItemPedido.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else


            return ItemPedido.createCriteria().list(criteria)

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

            if (pars.dataInicial){

                gt('dateCreated', pars.dataInicial)
            }

            if (pars.dataFinal) {

                lt('dateCreated', pars.dataFinal)
            }

        }

        return ItemPedido.createCriteria().count(criteria)
    }


    }
