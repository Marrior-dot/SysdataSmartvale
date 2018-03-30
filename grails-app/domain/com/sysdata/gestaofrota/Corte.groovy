package com.sysdata.gestaofrota

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa
import org.jrimum.domkee.financeiro.banco.febraban.Cedente
import org.jrimum.domkee.financeiro.banco.febraban.Sacado
import org.jrimum.domkee.comum.pessoa.endereco.Endereco

import com.sysdata.gestaofrota.Endereco as EnderecoFleet


class Corte {

    Date dateCreated
    Date dataPrevista
    Date dataFechamento
    Date dataCobranca
    Date dataInicioCiclo
    StatusCorte status
    Boolean liberado=false

    static belongsTo = [fechamento: Fechamento]

    static constraints = {
        dataFechamento nullable: true
    }

    String toString(){
        "#${this.id} dt.prev:${this.dataPrevista.format('dd/MM/yyyy')} dt.cob:${this.dataCobranca.format('dd/MM/yyyy')}"
    }


    private void tratarAtraso(Fatura fatRh,dataProc){
        Conta contaRh=fatRh.conta
        Rh rh=contaRh.participante
        def taxMulta=rh.multaAtraso
        def taxMora=rh.jurosProRata

        def pgtos=fatRh.itens.findAll{it.lancamento.tipo==TipoLancamento.PAGAMENTO}.sort{it.data}
        if(pgtos){

            def saldoDevedor=contaRh.ultimaFatura.valorTotal
            def dataCtrl=dataProc
            def multa=0.0
            def mora=0.0

            pgtos.each{pg->
                def delta=pg.data-dataCtrl
                if(delta>0 && saldoDevedor>0){
                    if(!multa) multa=(saldoDevedor*taxMulta/100).round(2)
                    mora+=(saldoDevedor*(taxMora/delta)/100).round(2)
                }
                saldoDevedor+=pg.valor
                if(pg.data>dataProc)
                    dataCtrl=pg.data
            }

            if(multa>0){
                LancamentoConvenio lcnMulta=new LancamentoConvenio()
                lcnMulta.with {
                    conta=contaRh
                    tipo=TipoLancamento.MULTA
                    dataEfetivacao=dataProc
                    valor=multa
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnMulta.save()
                ItemFatura itemMulta=new ItemFatura()
                itemMulta.with{
                    data=lcnMulta.dataEfetivacao
                    descricao=lcnMulta.tipo.nome
                    valor=lcnMulta.valor
                    lancamento=lcnMulta
                }
                fatRh.addToItens itemMulta
                fatRh.save()
            }
            if(mora>0){
                LancamentoConvenio lcnMora=new LancamentoConvenio()
                lcnMora.with {
                    conta=contaRh
                    tipo=TipoLancamento.MORA
                    dataEfetivacao=dataProc
                    valor=multa
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                }
                lcnMora.save()
                ItemFatura itemMora=new ItemFatura()
                itemMora.with{
                    data=lcnMora.dataEfetivacao
                    descricao=lcnMora.tipo.nome
                    valor=lcnMora.valor
                    lancamento=lcnMora
                }
                fatRh.addToItens itemMora
                fatRh.save()
            }
        }
    }

    private Corte fechar(dataProc){
        //Define data de fechamento (corte) para a data de processamento

        this.status=StatusCorte.FECHADO

        Fechamento fechAberto=this.fechamento

        //Se próximo fechamento
        Fechamento fechProx
        def fechList=this.fechamento.programa.fechamentos.sort{it.diaCorte}
        def fechIter=fechList.iterator()
        if(fechIter.hasNext()) fechProx=fechIter.next()
        else fechProx=fechList[0]

        def dataCorte=dataProc
        Calendar cal=dataCorte.toCalendar()

        if(fechProx.diaCorte>cal.get(Calendar.DAY_OF_MONTH)){
            cal.set(Calendar.DAY_OF_MONTH,fechProx.diaCorte)
        }else{
            cal.set(Calendar.DAY_OF_MONTH,fechProx.diaCorte)
            cal.add(Calendar.MONTH,1)
        }

        Corte corteProx=new Corte()
        corteProx.with{
            fechamento=fechProx
            dataPrevista=cal.time.clearTime()
            dataFechamento=cal.time.clearTime()
            dataCobranca=cal.time.clearTime()+fechProx.diasAteVencimento
            dataInicioCiclo=this.dataFechamento+1
            status=StatusCorte.ABERTO
        }
        corteProx.save()
        corteProx

    }


    private Endereco montarEndereco(participante){

        EnderecoFleet domainEndereco = participante.endereco
        if (domainEndereco == null) throw new Exception("Endereço não encontrado")
        Endereco endereco = new Endereco()
        String siglaUF = domainEndereco.cidade?.estado?.uf?.toUpperCase() ?: domainEndereco.cidade?.estado?.uf?.toUpperCase()
        endereco.setUF(UnidadeFederativa.valueOfSigla(siglaUF))
        endereco.setLocalidade(domainEndereco.cidade?.nome)
        endereco.setCep(domainEndereco.cep)
        endereco.setBairro(domainEndereco.bairro)
        endereco.setLogradouro(domainEndereco.logradouro)
        endereco.setNumero(domainEndereco.numero)

        return endereco


    }


    private void gerarBoleto(Fatura fatura){

        def docCedente
        def docSacado


        Administradora adm=Administradora.all[0]
        Rh rh=fatura.conta.participante as Rh

        Cedente cedente=new Cedente(adm.nome,docCedente)
        Sacado sacado=new Sacado(rh.nome,docSacado)
        sacado.addEndereco(montarEndereco(rh))






    }


    /**
     * Fatura todas as contas individuais (portadores)
     */
    void faturar(dataProc){

        def contasId=Portador.withCriteria {
            projections {
                property("conta.id")
            }
            unidade{
                rh{
                    fechamentos{
                        eq("id",this.fechamento.id)
                    }
                }
            }
            order("conta.id")
        }

        if(contasId.isEmpty()) log.info "Nao ha contas a faturar para este corte"
        else {
            this.dataFechamento=dataProc

            log.info "Total de Contas a Faturar: ${contasId.size()}"
            Conta contaRh=this.fechamento.programa.conta
            Fatura fatRh=new Fatura( )
            fatRh.with{
                conta=contaRh
                corte=this
                data=dataProc
                dataVencimento=this.dataCobranca
                status=StatusFatura.ABERTA
            }
            tratarAtraso(fatRh,dataProc)
            def itensFatRh=[:]
            contasId.each{
                Conta conta=Conta.get(it)
                Fatura fatPort=conta.portador.faturar(this,dataProc)
                fatPort.itens.each{itf->
                    if(!itensFatRh.containsKey(itf.lancamento.tipo)) itensFatRh[itf.lancamento.tipo]=0.0
                    itensFatRh[itf.lancamento.tipo]+=itf.valor
                }
            }
            //Gera lançamentos e itens da fatura Convênio
            itensFatRh.each{k,v->
                LancamentoConvenio lcto=new LancamentoConvenio()
                lcto.with{
                    conta=contaRh
                    tipo=k
                    dataEfetivacao=dataProc
                    valor=v
                    status=StatusLancamento.EFETIVADO
                    statusFaturamento=StatusFaturamento.FATURADO
                    corte=fatRh.corte
                }
                lcto.save()
                ItemFatura itemFatura=new ItemFatura()
                itemFatura.with{
                    data=lcto.dataEfetivacao
                    descricao=lcto.tipo.nome
                    valor=lcto.valor
                    lancamento=lcto
                    saldo=lcto.valor
                }
                fatRh.addToItens itemFatura
                fatRh.save()
            }

            //Gera boleto
            this.gerarBoleto(fatRh)

            //Fecha última fatura e transfere saldo

            Corte proxCorte=this.fechar(dataProc)

            //Gerar próximo corte
            def totalFatura=fatRh.valorTotal

            LancamentoConvenio fechamento=new LancamentoConvenio()
            fechamento.with{
                conta=fatRh.conta
                valor=-totalFatura
                tipo=TipoLancamento.FECHAMENTO_FATURA
                dataEfetivacao=dataProc
                corte=fatRh.corte
                statusFaturamento=StatusFaturamento.FATURADO
            }
            fechamento.save()
            LancamentoConvenio saldoAnterior=new LancamentoConvenio()
            saldoAnterior.with{
                conta=fatRh.conta
                valor=totalFatura
                tipo=TipoLancamento.SALDO_ANTERIOR
                dataEfetivacao=dataProc
                corte=proxCorte
                statusFaturamento=StatusFaturamento.NAO_FATURADO
            }
            saldoAnterior.save()
        }
        this.save()
    }
}
