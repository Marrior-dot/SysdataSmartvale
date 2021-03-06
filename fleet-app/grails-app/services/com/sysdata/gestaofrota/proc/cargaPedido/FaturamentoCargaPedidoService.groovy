package com.sysdata.gestaofrota.proc.cargaPedido

import com.fourLions.processingControl.ExecutableProcessing
import com.sysdata.gestaofrota.*
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoleto
import com.sysdata.gestaofrota.proc.faturamento.boleto.GeradorBoletoFactory
import com.sysdata.gestaofrota.proc.faturamento.notafiscal.GeradorNotaFiscal
import com.sysdata.gestaofrota.proc.faturamento.notafiscal.GeradorNotaFiscalFactory
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class FaturamentoCargaPedidoService implements ExecutableProcessing {

    GrailsApplication grailsApplication

    @Override
    def execute(Date date) {

        List<PedidoCargaInstancia> pedidoCargaList = PedidoCargaInstancia.findAllByStatus(StatusPedidoCarga.AGENDADO, [sort: "dataCarga"])

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

                    ItemFatura itemFatura = new ItemFatura()

                    if (item.tipo == TipoItemPedido.CARGA) {
                        LancamentoPortador lancamentoPortador = item.lancamento

                        itemFatura.with {
                            data = lancamentoPortador.dataEfetivacao
                            descricao = "CARGA CRT ${lancamentoPortador.transacao.cartao.numeroMascarado}"
                            valor = lancamentoPortador.valor
                            lancamento = lancamentoPortador
                        }

                        lancamentoPortador.status = StatusLancamento.FATURADO
                        lancamentoPortador.save()

                    } else if (item.tipo == TipoItemPedido.TAXA) {

                        LancamentoConvenio lancamentoConvenio = item.lancamento
                        itemFatura.with {
                            data = lancamentoConvenio.dataEfetivacao
                            descricao = lancamentoConvenio.tipo.nome
                            valor = lancamentoConvenio.valor
                            lancamento = lancamentoConvenio
                        }

                        lancamentoConvenio.status = StatusLancamento.FATURADO
                        lancamentoConvenio.save()

                    }

                    faturaEmpresa.addToItens(itemFatura)
                    faturaEmpresa.save(flush: true)

                }
                log.info "\t${faturaEmpresa}"


                pedido.fatura = faturaEmpresa
                pedido.status = StatusPedidoCarga.COBRANCA
                pedido.save(flush: true)


                if (grailsApplication.config.projeto.faturamento.portador.boleto.gerar) {

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

                if (grailsApplication.config.projeto.faturamento.portador.notaFiscal.gerar) {

                    GeradorNotaFiscal geradorNotaFiscal = GeradorNotaFiscalFactory.gerador
                    geradorNotaFiscal.gerarNotaFiscal(faturaEmpresa)

                }

            }

        } else
            log.warn "N??o h?? pedidos de carga para faturar"
    }
}
