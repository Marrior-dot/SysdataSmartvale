
package com.sysdata.gestaofrota.relatorios
import com.sysdata.gestaofrota.Veiculo
import grails.core.GrailsApplication

class BaseVeiculosService {


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

            if (params.placa) {

                eq("placa", params.placa)
            }



        }


        if (paginate)
            return Veiculo.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Veiculo.createCriteria().list(criteria)


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


            if (pars.placa) {

                eq("placa", pars.placa)
            }

        }

        return Veiculo.createCriteria().count(criteria)

    }






}