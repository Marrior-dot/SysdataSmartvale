package com.sysdata.gestaofrota.proc.agendamento

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Corte
import com.sysdata.gestaofrota.Estabelecimento
import com.sysdata.gestaofrota.Lancamento
import com.sysdata.gestaofrota.LancamentoEstabelecimento
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusFaturamento
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.StatusTransacao
import com.sysdata.gestaofrota.TipoCobranca
import com.sysdata.gestaofrota.TipoLancamento
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.proc.faturamento.CorteService
import grails.gorm.transactions.Transactional

@Transactional
class AgendamentoTransacaoService implements ExecutableProcessing {

    ReembolsoService reembolsoService
    CorteService corteService

    @Override
    def execute(Date date) {

        def agendarList = Transacao.where { status == StatusTransacao.AGENDAR }.list(sort: 'id')

        if (agendarList) {
            agendarList.each { tr ->
                agendarTransacao(tr, date)
            }
        } else
            log.warn "Não há transações para agendar!"
    }

    private def agendarTransacao(Transacao tr, Date dataRef) {

        def ret

        switch (tr.tipo) {
            case TipoTransacao.CARGA_SALDO:
                ret = agendarCarga(tr, dataRef)
                break
            case [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS]:
                ret = agendarCompra(tr, dataRef)
                break
            default:
                throw new RuntimeException("Tipo de Transação não tratada: ${tr.tipo}")
        }

        if (ret.success) {
            tr.status = StatusTransacao.AGENDADA
            tr.save(flush: true)
            log.info "TR #${tr.id} AG"
        } else {
            tr.discard()
            log.error "TR #${tr.id} NAO AGENDADA: " + ret.message
        }
    }

    private def agendarCarga(Transacao trCarga, Date dataRef) {
        def lancamentoInstance = new Lancamento(tipo: TipoLancamento.CARGA,
                                                status: StatusLancamento.EFETIVADO,
                                                valor: trCarga.valor,
                                                dataEfetivacao: dataRef,
                                                conta: trCarga.cartao.portador.conta)
        trCarga.addToLancamentos(lancamentoInstance)
        [success: true]
    }

    /**
     *
     * Se modelo de cobrança for PÓS-PAGO:
     *
     * - Procura o corte aberto para agendar lançamento portador
     *
     * Se modelo de cobrança for PRÉ-PAGO:
     *
     * - Agendamento Portador é apenas informativo
     */
    private def agendarCompra(Transacao abastInstance, Date dataRef) {

        def ret = [success: true]

        LancamentoPortador lctoCompra

        Rh rh = abastInstance.cartao.portador.unidade.rh

        if (rh.modeloCobranca == TipoCobranca.POS_PAGO) {
            Corte corteAberto = corteService.getCorteAberto(abastInstance.cartao.portador.unidade.rh)
            if (corteAberto) {
                lctoCompra = new LancamentoPortador()
                lctoCompra.with {
                    tipo = TipoLancamento.COMPRA
                    status = StatusLancamento.EFETIVADO
                    corte = corteAberto
                    valor = abastInstance.valor
                    conta = abastInstance.cartao.portador.conta
                    statusFaturamento = StatusFaturamento.NAO_FATURADO
                    dataEfetivacao = dataRef.clearTime()
                }
                lctoCompra.save flush: true
            }

        } else {
            //Lancamento funcionario
            lctoCompra = new LancamentoPortador(tipo: TipoLancamento.COMPRA,
                    status: StatusLancamento.EFETIVADO,
                    valor: abastInstance.valor,
                    dataEfetivacao: dataRef.clearTime(),
                    conta: abastInstance.cartao.portador.conta,
                    statusFaturamento: StatusFaturamento.NAO_FATURADO
            )
        }

        abastInstance.addToLancamentos(lctoCompra)
        //Lancamento estabelecimento
        def estabelecimentoInstance = Estabelecimento.findByCodigo(abastInstance.codigoEstabelecimento)
        if (!estabelecimentoInstance.empresa.taxaReembolso)
            throw new RuntimeException("Nao ha taxa adm nao definida para lojista #${estabelecimentoInstance.empresa.id}")
        def taxaAdm = estabelecimentoInstance.empresa.taxaReembolso
        BigDecimal valTxAdm = (abastInstance.valor * taxaAdm / 100.0)
        abastInstance.taxaAdm = taxaAdm
        abastInstance.valorReembolso = abastInstance.valor - valTxAdm
        //Arredonda
        def arrend = valTxAdm.round(2)
        def valReemb = abastInstance.valor - arrend
        def dataReembolso = reembolsoService.calcularDataReembolso(estabelecimentoInstance.empresa, dataRef)
        if (dataReembolso) {
            def lancReembolso = new LancamentoEstabelecimento(tipo: TipoLancamento.REEMBOLSO,
                    dataPrevista: dataReembolso,
                    status: StatusLancamento.A_EFETIVAR,
                    valor: valReemb,
                    valorTaxa: valTxAdm,
                    dataEfetivacao: dataReembolso,
                    conta: estabelecimentoInstance.empresa.conta,
                    statusFaturamento: StatusFaturamento.NAO_FATURADO)
            abastInstance.addToLancamentos(lancReembolso)
        } else {
            ret.success = false
            ret.message = "Reembolso não definido para o CNPJ: ${estabelecimentoInstance.empresa.cnpj}"
        }
        return ret
    }


}
