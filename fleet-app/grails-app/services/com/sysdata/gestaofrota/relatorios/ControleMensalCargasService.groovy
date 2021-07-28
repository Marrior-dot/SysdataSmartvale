package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.ItemPedidoMaquina
import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.ItemPedido
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.Util
import com.sysdata.gestaofrota.Veiculo

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

/*
        List fields = ["pedido.unidade.rh.nomeFantasia",
                       "pedido.unidade.nome",
                       "pedido.id",
                       "pedido.dateCreated",
                       "pedido.dataCarga",
                       "pedido.status",
                       "valor",
                       "pedido.itens.valor.sum()",
                       "pedido.taxa",
                       "pedido.taxaDesconto",
                       "pedido.validade",
        ]
*/


        def itensPedidoList
        if (paginate)
            itensPedidoList = ItemPedido.createCriteria().list([max: params.max, offset: params.offset], criteria)
        else
            itensPedidoList = ItemPedido.createCriteria().list(criteria)

        itensPedidoList = itensPedidoList.collect {
            [
                cliente: it.pedido.unidade.rh.nomeFantasia,
                unidade: it.pedido.unidade.nome,
                pedidoId: it.pedido.id,
                pedidoDataCriacao: it.pedido.dateCreated,
                pedidoDataCarga: it.pedido.dataCarga,
                identificadorMaquina: it.maquina.identificacaoCompacta,
                valor: Util.formatCurrency(it.valor),
                pedidoTotal: Util.formatCurrency(it.pedido.itens.sum { it.valor }),
                pedidoTaxa: it.pedido.taxa,
                pedidoTaxaDesconto: it.pedido.taxaDesconto,
                pedidoValidade: it.pedido.validade
            ]
        }
        return itensPedidoList

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
