package com.sysdata.gestaofrota.proc.faturamento.ext.portador

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.PortadorFuncionario
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import groovy.util.logging.Log4j

/**
 * Created by acception on 20/03/18.
 */

@Log4j
class TaxaManutencao implements ExtensaoFaturamento {

    /**
     * Cobra taxa:
     * 1. Se portador de cartão (funcionário/máquina) estiver ATIVO
     * ou
     * 2. Se taxa de utilização já tiver sido cobrada
     */

    @Override
    void tratar(ctx) {

        Conta cnt=ctx.fatura.conta
        Portador portador=ctx.portador

        def taxManut=portador.unidade.rh.taxaManutencao

        if(taxManut>0){

            Fatura fatura=ctx.fatura
            if(portador.ativo || fatura.itens.find{it.lancamento.tipo==TipoLancamento.TAXA_UTILIZACAO}){
                LancamentoPortador lcnTaxManut=new LancamentoPortador()
                lcnTaxManut.with{
                    corte=fatura.corte
                    conta=cnt
                    tipo=TipoLancamento.TAXA_MANUTENCAO
                    dataEfetivacao=ctx.dataProcessamento
                    valor=taxManut
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnTaxManut.save(failOnError:true)


                ItemFatura itTxMan=new ItemFatura()
                itTxMan.with{
                    data=lcnTaxManut.dataEfetivacao
                    descricao=lcnTaxManut.tipo.nome
                    valor=lcnTaxManut.valor
                    lancamento=lcnTaxManut
                }
                fatura.addToItens itTxMan
                fatura.save()

            }

        }
    }
}
