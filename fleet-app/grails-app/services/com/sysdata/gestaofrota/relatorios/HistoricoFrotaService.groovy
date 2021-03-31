
package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.TipoAbastecimento
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Veiculo
import com.sysdata.gestaofrota.Transacao

class HistoricoFrotaService {

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
                gt('dateCreated', pars.dataInicio)
            if (pars.dataFim)
                lt('dateCreated', pars.dataFim)
        }
        clo(criteria)
    }

    def list(pars, paginate = true) {
        withParams(pars) { criteria ->
            if (paginate)
                return Transacao.createCriteria().list([max: pars.max, offset: pars.offset], criteria)
            else
                return Transacao.createCriteria().list(criteria)
        }
    }

    def count(pars) {
        withParams(pars) { criteria ->
            return Transacao.createCriteria().count(criteria)
        }
    }
}


