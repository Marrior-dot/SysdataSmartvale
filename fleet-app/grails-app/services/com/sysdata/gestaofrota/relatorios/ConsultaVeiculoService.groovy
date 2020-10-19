
package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Veiculo
import com.sysdata.gestaofrota.Transacao

class ConsultaVeiculoService {



    def list(params, paginate = true) {
       // Transacao transacao = Transacao.get(params.id as long)

           // if (transacao.getTipo() == TipoTransacao.COMBUSTIVEL || transacao == TipoTransacao.SERVICOS) {

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

                    if (params.placa) {

                        eq("placa", params.placa)
                    }


                }


                if (paginate)
                    return Transacao.createCriteria().list([max: params.max, offset: params.offset], criteria)
                else
                    return Transacao.createCriteria().list(criteria)


            }



        def count(pars) {

         //   Transacao transacao = Transacao.get(params.id as long)

          //  if (transacao.getTipo()  == TipoTransacao.COMBUSTIVEL || transacao == TipoTransacao.SERVICOS) {

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


                if (pars.placa) {

                    eq("placa", pars.placa)
                }

            }

            return Transacao.createCriteria().count(criteria)

        }
    }


    


