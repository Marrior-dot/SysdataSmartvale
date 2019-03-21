package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.ReferenceDateProcessing
import grails.orm.PagedResultList
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class TransacaoService {

    static transactional = false

    private def ret = [:]

    def agendarTransacao(transacaoInstance) {
        if (transacaoInstance.status == StatusTransacao.AGENDAR) {

            if (transacaoInstance.tipo == TipoTransacao.CARGA_SALDO)
                agendarCarga(transacaoInstance)
            if (transacaoInstance.tipo == TipoTransacao.COMBUSTIVEL)
                agendarAbastecimento(transacaoInstance)

            if (ret.ok) {
                transacaoInstance.status = StatusTransacao.AGENDADA

                if (!transacaoInstance.save(flush: true)) {
                    ret.ok = false

                    transacaoInstance.errors.allErrors.each { err ->
                        ret.msg += "TR [${transacaoInstance.id}]: ${err.defaultMessage}\n"
                    }
                }
            } else
                transacaoInstance.discard()
        }
    }

    def agendarCarga(cargaInstance) {
        def contaInstance = cargaInstance.participante.conta
        def lancamentoInstance = new Lancamento(tipo: TipoLancamento.CARGA,
                status: StatusLancamento.EFETIVADO,
                valor: cargaInstance.valor,
                dataEfetivacao: cargaInstance.dateCreated,
                conta: contaInstance)
        cargaInstance.addToLancamentos(lancamentoInstance)
        //Atualiza saldo conta
        contaInstance.updateSaldo(lancamentoInstance.valor)
        contaInstance.save()

        ret.ok = true
    }

    def calcularDataReembolso(posto, data) {

        def cal = Calendar.getInstance()
        cal.setTime(data)
        def diaTr = cal.get(Calendar.DAY_OF_MONTH)
        def mesTr = cal.get(Calendar.MONTH) + 1
        def anoTr = cal.get(Calendar.YEAR)

        def fimDeSem = cal.get(Calendar.DAY_OF_WEEK)

        def calHoje = Calendar.getInstance()
        calHoje.setTime(new Date())
        def diaHj = calHoje.get(Calendar.DAY_OF_MONTH) + 1


        def reembInstance = posto.reembolsos.find { r -> diaTr >= r.inicioIntervalo && diaTr <= r.fimIntervalo }

        def dataReembolso

        if (reembInstance) {
            def diaReemb = reembInstance.diaEfetivacao
            def mesReemb = mesTr + reembInstance.meses
            def anoReemb = anoTr

            if (mesReemb > 12) {
                mesReemb = 1
                anoReemb = anoTr + 1
            }

            def calReemb = Calendar.getInstance()
            calReemb.set(anoReemb, --mesReemb, diaReemb)

            def dia = calReemb.get(Calendar.DAY_OF_WEEK)
            dataReembolso = calReemb.getTime()

            if (dia == 7) {

                dataReembolso += 2

            } else if (dia == 1) {

                dataReembolso += 1

            }
        }
        dataReembolso
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
    def agendarAbastecimento(Transacao abastInstance) {

        def dataProc = ReferenceDateProcessing.calcuteReferenceDate()

        LancamentoPortador lctoCompra

        Rh rh = abastInstance.cartao.portador.unidade.rh

        if (rh.modeloCobranca == TipoCobranca.POS_PAGO) {
            Corte corteAberto = abastInstance.cartao.portador.unidade.rh.corteAberto
            if (corteAberto) {
                lctoCompra = new LancamentoPortador()
                lctoCompra.with {
                    tipo = TipoLancamento.COMPRA
                    status = StatusLancamento.EFETIVADO
                    corte = corteAberto
                    valor = abastInstance.valor
                    conta = abastInstance.cartao.portador.conta
                    statusFaturamento = StatusFaturamento.NAO_FATURADO
                    dataEfetivacao = dataProc
                }
                lctoCompra.save flush: true
            }

        } else {
            //Lancamento funcionario
            lctoCompra = new LancamentoPortador(tipo: TipoLancamento.COMPRA,
                    status: StatusLancamento.EFETIVADO,
                    valor: abastInstance.valor,
                    dataEfetivacao: dataProc,
                    conta: abastInstance.participante.conta,
                    statusFaturamento: StatusFaturamento.NAO_FATURADO
            )
        }

        abastInstance.addToLancamentos(lctoCompra)
        //Lancamento estabelecimento
        def estabelecimentoInstance = Estabelecimento.findByCodigo(abastInstance.codigoEstabelecimento)
        if (!estabelecimentoInstance.empresa.taxaReembolso) throw new RuntimeException("Nao ha taxa adm nao definida para lojista #${estabelecimentoInstance.empresa.id}")
        def taxaAdm = estabelecimentoInstance.empresa.taxaReembolso
        BigDecimal valTxAdm = (abastInstance.valor * taxaAdm / 100.0)
        abastInstance.taxaAdm = taxaAdm
        abastInstance.valorReembolso = abastInstance.valor - valTxAdm
        //Arredonda
        def arrend = valTxAdm.round(2)
        def valReemb = abastInstance.valor - arrend
        def dataReembolso = calcularDataReembolso(estabelecimentoInstance.empresa, dataProc)
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
            ret.ok = true
        } else {
            ret.ok = false
            ret.msg = "Reembolso não definido para o CNPJ:${estabelecimentoInstance.empresa.cnpj}"
        }
    }

    def agendarAll() {

        ret.clear()

        log.info "Gerando lancamentos financeiros a partir das transacoes..."

        def agendarList = Transacao.findAllWhere(status: StatusTransacao.AGENDAR)

        agendarList.each { tr ->
            agendarTransacao(tr)
            if (ret.ok)
                log.debug "Transacao #${tr.id}  AGENDADA"
            else {
                log.debug "Transacao #${tr.id}  NAO AGENDADA - " + ret.msg
                throw new RuntimeException("Transacao #${tr.id}  NAO AGENDADA")
            }
        }

        log.info agendarList.size() > 0 ? "Lancamentos financeiros gerados" : "Nao ha lancamentos financeiros para agendar"

        ret
    }

    /**
     * Realiza a pesquisa de transações com o filtro e paginação repassados
     * @param participante
     * @param filtro
     * @param paginacao
     * @return Um PagedResultList contendo a pesquisa paginada
     */
    PagedResultList pesquisar(final Participante participante, final Map filtro, final Map paginacao) {
        Unidade unidade
        Estabelecimento estabelecimento

        if (SpringSecurityUtils.ifAllGranted("ROLE_ESTAB") && participante?.instanceOf(PostoCombustivel)) {
            PostoCombustivel postoCombustivel = PostoCombustivel.get(participante.id)
            estabelecimento = Estabelecimento.findByEmpresa(postoCombustivel)
        } else if (SpringSecurityUtils.ifAllGranted("ROLE_RH") && participante?.instanceOf(Rh)) {
            Rh rh = Rh.get(participante.id)
            unidade = Unidade.findByRh(rh)
        }

        return Transacao.createCriteria().list(paginacao) {
            if (filtro.dataInicial) gt('dateCreated', filtro.dataInicial)
            if (filtro.dataFinal) lt('dateCreated', filtro.dataFinal)
            if (filtro.numeroCartao) eq("numeroCartao", filtro.numeroCartao)
            if (filtro.codigoEstabelecimento) eq("codigoEstabelecimento", filtro.codigoEstabelecimento)
            if (filtro.nsu) eq("nsu", filtro.nsu)
            if (filtro.tipos) 'in'('tipo', filtro.tipos)
            if (filtro.statusControle) {
                if (filtro.statusControle instanceof StatusControleAutorizacao)
                    eq('statusControle', filtro.statusControle)
                else eq('statusControle', StatusControleAutorizacao.valueOf(filtro.statusControle.toString()))
            }

            if (unidade) {
                'participante' {
                    eq('unidade', unidade)
                }
            }
            if (estabelecimento?.codigo?.length() > 0) {
                eq("codigoEstabelecimento", estabelecimento.codigo)
            }
        }
    }

    void confirmar(List<Long> ids) {
        ids.each { confirmar(Transacao.get(it)) }
    }

    Transacao confirmar(Transacao transacao) {
        if (transacao == null) return

        if (transacao.tipo == TipoTransacao.COMBUSTIVEL || transacao.tipo == TipoTransacao.SERVICOS) {
            transacao.statusControle = StatusControleAutorizacao.CONFIRMADA
            transacao.status = StatusTransacao.AGENDAR
        } else if (transacao.tipo == TipoTransacao.CONFIGURACAO_PRECO) {
            transacao.statusControle = StatusControleAutorizacao.CONFIRMADA
        }

        transacao.save()
    }

    void desfazer(List<Long> ids) {
        ids.each { desfazer(Transacao.get(it)) }
    }

    Transacao desfazer(Transacao transacao) {
        if (transacao == null) return

        if (transacao.tipo == TipoTransacao.COMBUSTIVEL || transacao.tipo == TipoTransacao.SERVICOS) {
            transacao.statusControle = StatusControleAutorizacao.DESFEITA
            transacao.participante.conta.saldo += transacao.valor
        } else if (transacao.tipo == TipoTransacao.CONFIGURACAO_PRECO) {
            transacao.statusControle = StatusControleAutorizacao.DESFEITA
        }

        transacao.save()
    }
}
