
package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Veiculo

class BaseEquipamentosService {


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

            if (params.codigo) {

                eq("codigo", params.codigo)
            }



        }


        if (paginate)
            return Equipamento.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            return Equipamento.createCriteria().list(criteria)


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


            if (pars.codigo) {

                eq("codigo", pars.codigo)
            }

        }

        return Equipamento.createCriteria().count(criteria)

    }






}