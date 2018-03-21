package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.Transacao

/**
 * Created by acception on 21/03/18.
 */
class TaxaAdministracao implements ExtensaoFaturamento {

    @Override
    def tratar(ctx) {
        Conta cnt=ctx.conta
        Portador portador=cnt.participante as Portador
        def taxAdm=portador.unidade.rh.taxaAdministracao
        if(taxAdm>0){

            Corte corte=ctx.fatura.corte
            def totalAprov=Transacao.withCriteria {
                                projections {
                                    sum("valor")
                                }
                                cartao{
                                    eq("portador",portador)
                                }
                                'in'("statusControle",[StatusControleAutorizacao.CONFIRMADA,
                                                       StatusControleAutorizacao.PENDENTE])
                                ge("dataHora",corte.dataInicioCiclo)
                                le("dataHora",corte.dataFechamento)
                            }
            if(totalAprov>0){

                Fatura fatura=ctx.fatura
                def valTaxAdm=(totalAprov*taxAdm/100.00).round(2)
                LancamentoPortador lcnTaxaAdm=new LancamentoPortador()
                lcnTaxaAdm.with {
                    conta=cnt
                    tipo=TipoLancamento.TAXA_ADM
                    dataEfetivacao=new Date().clearTime()
                    valor=valTaxAdm
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnTaxaAdm.save()

                ItemFatura itTxAdm=new ItemFatura()
                itTxAdm.with{
                    data=lcnTaxaAdm.dataEfetivacao
                    descricao=lcnTaxaAdm.tipo.nome
                    valor=lcnTaxaAdm.valor
                    lancamento=lcnTaxaAdm
                }
                fatura.addToItens itTxAdm
                fatura.save()
            }
        }

    }
}
