package com.sysdata.gestaofrota.proc.cargaPedido

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.Boleto
import com.sysdata.gestaofrota.Conta
import com.sysdata.gestaofrota.Fatura
import com.sysdata.gestaofrota.ItemFatura
import com.sysdata.gestaofrota.LancamentoPortador
import com.sysdata.gestaofrota.PedidoCarga
import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.StatusEmissao
import com.sysdata.gestaofrota.StatusFatura
import com.sysdata.gestaofrota.StatusGeracaoBoleto
import com.sysdata.gestaofrota.StatusLancamento
import com.sysdata.gestaofrota.StatusPedidoCarga
import com.sysdata.gestaofrota.TipoFatura
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoleto
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoletoFactory
import com.sysdata.gestaofrota.proc.faturamento.notafiscal.GeracaoArquivoRPSBarueriService
import grails.gorm.transactions.Transactional

@Transactional
class FaturamentoCargaPedidoService implements ExecutableProcessing {

    GeracaoArquivoRPSBarueriService geracaoArquivoRPSBarueriService

    @Override
    def execute(Date date) {

        List<PedidoCarga> pedidoCargaList = PedidoCarga.findAllByStatus(StatusPedidoCarga.AGENDADO, [sort: "dataCarga"])

        if (!pedidoCargaList.isEmpty()) {

            pedidoCargaList.each { pedido ->

                log.info "Faturando Pedido de Carga #${pedido.id} ..."

                Rh empresa = pedido.unidade.rh
                Conta contaEmpresa = empresa.conta

                Fatura faturaEmpresa = new Fatura()
                faturaEmpresa.with {
                    conta = contaEmpresa
                    data = date
                    dataVencimento = date + 1
                    status = StatusFatura.ABERTA
                    tipo = TipoFatura.CONVENIO_PREPAGO
                }
                faturaEmpresa.save()

                pedido.itens.each { item ->

                    LancamentoPortador lancamentoPortador = (item.transacao.lancamentos as List)[0]

                    ItemFatura itemFatura = new ItemFatura()
                    itemFatura.with {
                        data = lancamentoPortador.dataEfetivacao
                        descricao = "CARGA CRT ${lancamentoPortador.transacao.cartao.numeroMascarado}"
                        valor = lancamentoPortador.valor
                        lancamento = lancamentoPortador
                    }

                    lancamentoPortador.status = StatusLancamento.FATURADO
                    lancamentoPortador.save()

                    faturaEmpresa.addToItens(itemFatura)
                    faturaEmpresa.save(flush: true)

                }
                log.info "\t${faturaEmpresa}"


                pedido.fatura = faturaEmpresa
                pedido.status = StatusPedidoCarga.COBRANCA
                pedido.save(flush: true)


                // Boleto
                Boleto boleto = new Boleto()
                boleto.with {
                    fatura = faturaEmpresa
                    dataVencimento = faturaEmpresa.dataVencimento
                    valor = faturaEmpresa.valorTotal
                }
                boleto.save(flush: true)

                GeradorBoleto geradorBoleto = GeradorBoletoFactory.gerador
                geradorBoleto.gerarBoleto(boleto)

                faturaEmpresa.statusGeracaoBoleto = StatusGeracaoBoleto.GERADO

                // Nota Fiscal
                faturaEmpresa.statusEmissao = StatusEmissao.GERAR_ARQUIVO
                faturaEmpresa.save()

            }

        } else
            log.warn "Não há pedidos de carga para faturar"
    }
}
