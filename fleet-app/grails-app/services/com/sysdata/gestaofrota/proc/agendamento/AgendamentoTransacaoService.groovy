package com.sysdata.gestaofrota.proc.agendamento

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.CorteService
import grails.gorm.transactions.Transactional

@Transactional
class AgendamentoTransacaoService implements ExecutableProcessing {

    ReembolsoService reembolsoService
    CorteService corteService

    @Override
    def execute(Date date) {

        def agendarList = Transacao.where { status == StatusTransacao.AGENDAR && tipo != TipoTransacao.CARGA_SALDO }.list(sort: 'id')

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
            CortePortador corteAberto = corteService.getCorteAberto(abastInstance.cartao.portador.unidade.rh)
            if (corteAberto) {
                lctoCompra = new LancamentoPortador()
                lctoCompra.with {
                    tipo = TipoLancamento.COMPRA
                    status = StatusLancamento.A_FATURAR
                    corte = corteAberto
                    valor = abastInstance.valor
                    conta = abastInstance.cartao.portador.conta
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
                    status: StatusLancamento.A_FATURAR,
                    valor: valReemb,
                    valorTaxa: valTxAdm,
                    dataEfetivacao: dataReembolso,
                    conta: estabelecimentoInstance.empresa.conta)
            abastInstance.addToLancamentos(lancReembolso)
        } else {
            ret.success = false
            ret.message = "Reembolso não definido para o CNPJ: ${estabelecimentoInstance.empresa.cnpj}"
        }
        return ret
    }


}
