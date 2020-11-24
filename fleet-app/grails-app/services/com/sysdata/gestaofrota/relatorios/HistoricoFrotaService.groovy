
package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoAbastecimento
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Veiculo
import com.sysdata.gestaofrota.Transacao

class HistoricoFrotaService {


    def list(params, paginate = true) {


        def criteria = {

            or {
                eq("tipo", TipoTransacao.COMBUSTIVEL)
                eq("tipo", TipoTransacao.SERVICOS)
            }

            eq("statusControle", StatusControleAutorizacao.CONFIRMADA)

            if (params.empresa) {

                cartao {

                    portador {

                        unidade {
                            rh {
                                eq("id", params.empresa.toLong())
                            }
                        }

                    }
                }
            }

            if (params.unidade) {

                cartao {

                    portador {

                        unidade {
                            eq("id", params.unidade.toLong())
                        }

                    }
                }
            }
            if (params.placa) {

                maquina {

                    eq("placa", params.placa)

                }

            }

            if (params.codigo) {

                maquina {

                    eq("codigo", params.codigo)
                }
            }

            if (params.dataInicio) gt('dateCreated', params.dataInicio)

            if (params.dataFim) lt('dateCreated', params.dataFim)






        }




        if (paginate)
            return Transacao.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Transacao.createCriteria().list(criteria)

    }

    def count(pars) {

        def criteria = {

            or {
                eq ("tipo", TipoTransacao.COMBUSTIVEL)
                eq ( "tipo", TipoTransacao.SERVICOS)
            }

            eq("statusControle", StatusControleAutorizacao.CONFIRMADA)



            if (pars.empresa) {

                cartao {

                    portador {

                        unidade {
                            rh {
                                eq("id", pars.empresa.toLong())
                            }
                        }

                    }
                }
            }

            if (pars.unidade) {

                cartao {

                    portador {

                        unidade {
                            eq("id", pars.unidade.toLong())
                        }

                    }
                }
            }



            if (pars.placa) {

                maquina{

                    eq("placa", pars.placa)

                }

            }

            if (pars.codigo){

                maquina{

                    eq("codigo", pars.codigo)
                }
            }


            if (pars.dataInicio) gt('dateCreated', pars.dataInicio)

            if (pars.dataFim) lt('dateCreated', pars.dataFim)


        }





        return Transacao.createCriteria().count(criteria)

    }
}


