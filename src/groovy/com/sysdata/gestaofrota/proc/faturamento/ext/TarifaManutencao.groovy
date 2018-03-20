package com.sysdata.gestaofrota.proc.faturamento.ext

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Portador
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.TipoLancamento
import groovy.util.logging.Log4j

/**
 * Created by acception on 20/03/18.
 */

@Log4j
class TarifaManutencao implements ExtensaoFaturamento {

    /**
     * Cobra tarifa se cartão atual do portador estiver ATIVO no momento do fechamento
     */

    @Override
    def tratar(ctx) {

        Conta cnt=ctx.conta
        Portador portador=cnt.participante as Portador

        def taxManut=portador.unidade.rh.taxaManutencao

        if(taxManut>0){

            Cartao cartao=portador.cartaoAtual

            if(cartao.status==StatusCartao.ATIVO){
                LancamentoPortador lcnTaxManut=new LancamentoPortador()
                lcnTaxManut.with{
                    conta=cnt
                    tipo=TipoLancamento.TAXA_MANUTENCAO
                    dataEfetivacao=new Date().clearTime()
                    valor=taxManut
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnTaxManut.save()

                Fatura fatura=ctx.fatura
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
