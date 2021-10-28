package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.*
import grails.gorm.transactions.Transactional

@Transactional
class RegeraLoteRecebimentoPorLoteRepasseService {

    FechamentoLoteRecebimentoService fechamentoLoteRecebimentoService

    def redo(LotePagamento lotePagamento) {
        def clients = [:]
        lotePagamento.pagamentos.each { paymentBatch ->
            log.info "PG LT #${paymentBatch.id} => EC: ${paymentBatch.estabelecimento.nome} VL: ${paymentBatch.valor}"
            paymentBatch.pagamentos.each { payment ->
                log.info "\tPG #${payment.id} => VL: ${payment.valor}"
                def entriesList = LancamentoEstabelecimento.findWhere(pagamento: payment)
                entriesList.each { entry ->
                    Rh rh = entry.transacao.cartao.portador.unidade.rh
                    log.info "\t\tLC #${entry.id} => RH:${rh.id} VL:${entry.valor} VL.TR.:${entry.transacao.valor} "
                    if (! clients.containsKey(rh.id))
                        clients[rh.id] = [liquido: entry.valor, bruto: entry.transacao.valor, comissao: entry.transacao.valor - entry.valor]
                    else {
                        clients[rh.id].liquido += entry.valor
                        clients[rh.id].bruto += entry.transacao.valor
                        clients[rh.id].comissao += (entry.transacao.valor - entry.valor)
                    }
                }
            }
        }
        log.info "Cortando RHs para recebimento ..."
        CorteConvenio corteConvenio = new CorteConvenio()
        corteConvenio.with {
            status = StatusCorte.FECHADO
            dataPrevista = lotePagamento.dataEfetivacao
            dataFechamento = lotePagamento.dataEfetivacao
            dataCobranca = lotePagamento.dataEfetivacao
        }
        corteConvenio.save(flush: true)
        log.info "Corte Convênio #${corteConvenio.id} criado"

        clients.each { rhId, valores ->
            RecebimentoConvenio recebimentoConvenio = new RecebimentoConvenio()
            recebimentoConvenio.with {
                dataProgramada = lotePagamento.dataEfetivacao
                rh = Rh.get(rhId)
                corte = corteConvenio
                valor = valores.liquido
                valorBruto = valores.bruto
                valorTaxaAdm = valores.comissao
            }
            recebimentoConvenio.save(flush: true)
            log.info "(+) REC #${recebimentoConvenio.id} => RH:${recebimentoConvenio.rh.id} - (bru: ${recebimentoConvenio.valorBruto} liq: ${recebimentoConvenio.valor} com: ${recebimentoConvenio.valorTaxaAdm})"
        }

        // Vincula Corte Convênio a Lote de Recebimento
        LoteRecebimento loteAberto = LoteRecebimento.aberto
        loteAberto.addToCortes(corteConvenio)
        loteAberto.save(flush: true)


        fechamentoLoteRecebimentoService.execute(new Date().clearTime())
    }
}
