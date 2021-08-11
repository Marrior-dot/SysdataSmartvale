
package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoAbastecimento
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Veiculo
import com.sysdata.gestaofrota.Transacao

class HistoricoFrotaService {

    /**
     *
     * @param pars
     * @param clo
     * @return
     *
     *
     * select
     *  t.*
     *     from
     *     Transacao t
     *
     * where
     *      (t.tipo = 'COMBUSTIVEL' or t.tipo = 'SERVICOS')
     *      and t.statusControle = 'CONFIRMADA'
     *
     *
     *
     *
     *
     *
     */

    private def withParams(pars, Closure clo) {

        def criteria = {

            or {
                eq("tipo", TipoTransacao.COMBUSTIVEL)
                eq("tipo", TipoTransacao.SERVICOS)
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
            if (pars.nFanta) {
                cartao {
                    portador {
                        unidade {
                            rh {
                                eq("nomeFantasia", pars.nFanta)
                            }
                        }
                    }
                }
            }
            if (pars.placa) {
                maquina {
                    eq("placa", pars.placa)
                }
            }
            if (pars.codigo) {
                maquina {
                    eq("codigo", pars.codigo)
                }
            }
            if (pars.dataInicio)
                ge('dateCreated', pars.date('dataInicio', 'dd/MM/yyyy'))
            if (pars.dataFim)
                le('dateCreated', pars.date('dataFim', 'dd/MM/yyyy'))
        }
        clo(criteria)
    }

    def list(pars, paginate = true) {
        pars.sort = "dateCreated"

        withParams(pars) { criteria ->
            if (paginate)
                return Transacao.createCriteria().list([max: pars.max, offset: pars.offset, sort: pars.sort], criteria)
            else {
                println "Params: $pars"
                return Transacao.createCriteria().list(criteria)
            }
        }
    }

    def count(pars) {
        withParams(pars) { criteria ->
            return Transacao.createCriteria().count(criteria)
        }
    }
}


