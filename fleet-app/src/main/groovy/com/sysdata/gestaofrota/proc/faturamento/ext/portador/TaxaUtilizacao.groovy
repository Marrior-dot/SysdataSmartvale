package com.sysdata.gestaofrota.proc.faturamento.ext.portador

import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento

/**
 * Created by acception on 20/03/18.
 */
class TaxaUtilizacao implements ExtensaoFaturamento {

    /**
     *
     * Cobra tarifa de utilização se cartão transacionou no período
     */

    void tratar(ctx) {
        Conta cnt=ctx.fatura.conta
        Portador portador=ctx.portador

        def taxUtiliz=portador.unidade.rh.taxaUtilizacao
        println "Taxa Utilizacao: $taxUtiliz"

        if(taxUtiliz>0){

            Fatura fatura=ctx.fatura
            Corte corteAtual=fatura.corte
            println "Corte (inicio:${corteAtual.dataInicioCiclo.format('dd/MM/yyyy')} fech:${corteAtual.dataFechamento.format('dd/MM/yyyy')})"

            def count=Transacao.withCriteria(uniqueResult:true){
                projections {
                    rowCount('id')
                }
                cartao{
                    eq("portador",portador)
                }
                'in'("statusControle",[StatusControleAutorizacao.PENDENTE,StatusControleAutorizacao.CONFIRMADA])
                ge("dataHora",corteAtual.dataInicioCiclo)
                le("dataHora",corteAtual.dataFechamento)
            }

            println "Qtde Trs: ${count}"

            if(count>0){


                LancamentoPortador lcnTaxUtiliz=new LancamentoPortador()
                lcnTaxUtiliz.with{
                    corte=corteAtual
                    conta=cnt
                    tipo=TipoLancamento.TAXA_UTILIZACAO
                    dataEfetivacao=ctx.dataProcessamento
                    valor=taxUtiliz
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnTaxUtiliz.save(failOnError:true)

                ItemFatura itTxUtil=new ItemFatura()
                itTxUtil.with{
                    data=lcnTaxUtiliz.dataEfetivacao
                    descricao=lcnTaxUtiliz.tipo.nome
                    valor=lcnTaxUtiliz.valor
                    lancamento=lcnTaxUtiliz
                }
                fatura.addToItens itTxUtil
                fatura.save()
            }
        }
    }

    @Override
    void gerarLancamento(Map ctx) {

    }

    @Override
    void calcularValor(Map ctx) {

    }
}
