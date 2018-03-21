package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.*

/**
 * Created by acception on 20/03/18.
 */
class TaxaUtilizacao implements ExtensaoFaturamento {

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
            Corte corte=fatura.corte

            def count=Transacao.withCriteria(uniqueResult:true){
                projections {
                    rowCount('id')
                }
                cartao{
                    eq("portador",portador)
                }
                'in'("statusControle",[StatusControleAutorizacao.PENDENTE,StatusControleAutorizacao.CONFIRMADA])
                ge("dataHora",corte.dataInicioCiclo)
                le("dataHora",corte.dataFechamento)
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
