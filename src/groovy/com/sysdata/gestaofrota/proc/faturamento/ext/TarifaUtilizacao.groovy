package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.StatusTransacao
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.Transacao

/**
 * Created by acception on 20/03/18.
 */
class TarifaUtilizacao implements ExtensaoFaturamento {

    /**
     *
     * Cobra tarifa de utilização se cartão transacionou no período
     */

    @Override
    def tratar(ctx) {
        Conta cnt=ctx.conta
        Portador portador=cnt.participante as Portador
        def taxUtiliz=portador.unidade.rh.taxaUtilizacao
        if(taxUtiliz>0){

            Fatura fatura=ctx.fatura

            def count=Transacao.withCriteria {
                projections {
                    rowCount('id')
                }
                cartao{
                    eq("portador",portador)
                }
                'in'("statusControle",[StatusControleAutorizacao.PENDENTE,StatusControleAutorizacao.CONFIRMADA])
            }

            if(count>0){
                LancamentoPortador lcnTaxUtiliz=new LancamentoPortador()
                taxUtiliz.with{
                    conta=cnt
                    tipo=TipoLancamento.TAXA_UTILIZACAO
                    dataEfetivacao=new Date().clearTime()
                    valor=taxUtiliz
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                taxUtiliz.save()

                ItemFatura itTxUtil=new ItemFatura()
                itTxUtil.with{
                    data=taxUtiliz.dataEfetivacao
                    descricao=taxUtiliz.tipo.nome
                    valor=taxUtiliz.valor
                    lancamento=taxUtiliz
                }
                fatura.addToItens itTxUtil
                fatura.save()
            }
        }
    }
}
