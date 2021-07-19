package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.ItemPedido
import com.sysdata.gestaofrota.Funcionario

class ControleMensalCargasService {

    def list(params, paginate = true) {

        def criteria = {


            if (params.empresa){

                pedido{
                    unidade {
                        rh {
                            eq("id", params.empresa.toLong())
                        }
                    }
                }}


            if (params.unidade) {

                pedido{

                    unidade {
                        eq("id", params.unidade.toLong())
                    }
                }
            }

            if (params.dataInicio){

                pedido{
                    gt('dateCreated', params.dataInicio)
                }
            }

            if (params.dataFim) {
                pedido{
                    lt('dateCreated', params.dataFim)
                }
            }
        }

        if (paginate)
            return ItemPedido.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else

            return ItemPedido.createCriteria().list(criteria)

    }

    def count(pars) {

        def criteria = {

            if (pars.empresa){

            pedido{
                unidade {
                    rh {
                        eq("id", pars.empresa.toLong())
                    }
                }
            }

            }
            if (pars.unidade) {

                pedido{
                    unidade {
                        eq("id", pars.unidade.toLong())
                    }

                }
            }


            if (pars.dataInicio){

                pedido{
                    gt('dateCreated', pars.dataInicio)
                }
            }

            if (pars.dataFim) {

                pedido{
                    lt('dateCreated', pars.dataFim)
                }
            }
        }

        return ItemPedido.createCriteria().count(criteria)
    }


    }
