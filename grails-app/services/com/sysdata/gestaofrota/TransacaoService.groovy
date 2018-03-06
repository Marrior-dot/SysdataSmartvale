package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.ReferenceDateProcessing

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

        def dataProc=ReferenceDateProcessing.calcuteReferenceDate()

        LancamentoPortador lctoCompra

        Rh rh=abastInstance.cartao.portador.unidade.rh

        if(rh.modeloCobranca==TipoCobranca.POS_PAGO){
            Corte corteAberto=abastInstance.cartao.portador.unidade.rh.corteAberto
            if(corteAberto) {
                lctoCompra=new LancamentoPortador()
                lctoCompra.with{
                    tipo=TipoLancamento.COMPRA
                    status=StatusLancamento.A_EFETIVAR
                    corte=corteAberto
                    valor=abastInstance.valor
                    conta=abastInstance.cartao.portador.conta
                    statusFaturamento=StatusFaturamento.NAO_FATURADO
                    dataEfetivacao=dataProc
                }
                lctoCompra.save flush: true
            }

        }else{
            //Lancamento funcionario
            lctoCompra=new LancamentoPortador(tipo: TipoLancamento.COMPRA,
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
        double valoReemb = (abastInstance.valor * estabelecimentoInstance.empresa.taxaReembolso / 100.0)
        //Arredonda
        def arrend = Util.roundCurrency(valoReemb)

        def dataReembolso = calcularDataReembolso(estabelecimentoInstance.empresa,dataProc )

        if (dataReembolso) {
            def lancReembolso = new Lancamento(tipo: TipoLancamento.REEMBOLSO,
                    status: StatusLancamento.A_EFETIVAR,
                    valor: abastInstance.valor - arrend,
                    dataEfetivacao: dataReembolso,
                    conta: estabelecimentoInstance.empresa.conta)
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
            else
                log.debug "Transacao #${tr.id}  NAO AGENDADA - " + ret.msg
        }

        log.info agendarList.size() > 0 ? "Lancamentos financeiros gerados" : "Nao ha lancamentos financeiros para agendar"

        ret
    }


}
