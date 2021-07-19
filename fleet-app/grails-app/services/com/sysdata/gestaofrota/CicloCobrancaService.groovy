package com.sysdata.gestaofrota


class CicloCobrancaService {



    private def withCobranca={Funcionario f,TipoLancamento tl,BigDecimal tx,Closure cls->

        def cicloAberto=CicloCobranca.cicloAberto

        def lcnPend=Lancamento.withCriteria {
            eq("tipo",tl)
            eq("status",StatusLancamento.A_FATURAR)
            eq("conta",f.conta)
        }

        if (lcnPend) {
            //Se lançamento a efetivar com ciclo diferente do ciclo aberto, altera a referencia para este
            if (lcnPend.referencia!=cicloAberto.referencia) {
                lcnPend.referencia=cicloAberto.referencia
                lcnPend.save(flush:true)
            }
        }else{
            def evt=cls.call()

            if(evt){
                def lanc=new Lancamento()
                lanc.with{
                    conta=f.conta
                    valor=tx
                    tipo=tl
                    referencia=cicloAberto.referencia
                    status=StatusLancamento.A_FATURAR
                    save(flush:true)
                }
            }

        }

    }


/**
 * Será cobrado Taxa de Utilização para qualquer portador
 * que tenha realizado transação de Compra de Combustível e Serviço
 * no mês anterior
  */

    private def cobrarTaxaUtilizacao(f,intv,tx){

        withCobranca(f,TipoLancamento.TAXA_UTILIZACAO,tx){

            def evt=(Transacao.withCriteria(uniqueResult:true) {
                                    "inList"("tipo",[TipoTransacao.COMBUSTIVEL,TipoTransacao.SERVICOS])
                                    "inList"("status",[StatusControleAutorizacao.PENDENTE,StatusControleAutorizacao.CONFIRMADA])
                                    eq("participante",f)
                                    ge("dateCreated",intv.prmDat)
                                    le("dateCreated",intv.ultDat)
                                    projections {
                                        rowCount("id")
                                    }
                                })>0
            evt
        }
    }

    /**
     * Será cobrada Mensalidade para qualquer portador que estiver ATIVO no mês anterior
     */

    private def cobrarMensalidade(f,intv,tx){

        withCobranca(f,TipoLancamento.MENSALIDADE,tx){

            def ativoMesAnt=(AuditLogEvent.withCriteria(uniqueResult:true) {
                eq("className","Funcionario")
                eq("propertyName","status")
                eq("oldValue","ATIVO")
                eq("newValue","INATIVO")
                ge("dateCreated",intv.prmDat)
                le("dateCreated",intv.ultDat)
                projections{
                    rowCount("id")
                }
            })>0

            def evt=(f.status==Status.ATIVO && f.dateCreated<=intv.ultDat) || (f.status==Status.INATIVO && ativoMesAnt)
            evt
        }

    }

    private def cobrarEmissaoCartao(f,intv,tx){

        withCobranca(f,TipoLancamento.EMISSAO_CARTAO,tx){

            def carList=f.cartoes as List<Cartao>
            carList.size()==1 && carList[0].dateCreated>=intv.prmDat && carList[0].dateCreated<=intv.ultDat
        }

    }

    private def cobrarReemissaoCartao(f,intv,tx){

        withCobranca(f,TipoLancamento.REEMISSAO_CARTAO,tx) {

            def evt=false
            if(f.cartoes.size()>1) {
                //Desconsidera primeiro cartão na pesquisa
                def carList=f.cartoes as List<Cartao>
                def prim=carList.sort{it.id}[0]
                def outList=carList-prim
                evt=outList.any{it.dateCreated.clearTime()>=intv.primDat && it.dateCreated.clearTime()<=intv.ultDat}
            }
            evt
        }
    }


    private def calcularIntervaloReferencia(dataRef){
        def dt=dataRef.clearTime()
        def mes=dataRef[Calendar.MONTH]
        def ano=dataRef[Calendar.YEAR]
        if(mes-1<1){
            ano--
            mes=12
        }else mes--
        //Primeira data intervalo
        dt.set(year:ano,month:mes,date:1)
        def ultDiaMes=dt.toCalendar().getActualMaximum(Calendar.DAY_OF_MONTH)
        def ultDia=dt.toCalendar().time
        //Ultima data intervalo
        ultDia.set(year:ano,month:mes,date:ultDiaMes)
        [prmDat:dt,ultDat:ultDia]
    }

    def execute(dateRef) {

        log.debug "Iniciando Cobranca de Taxas Cartoes..."
        if(dateRef){
            def intervRef=calcularIntervaloReferencia(dateRef)
            log.debug "Periodo de referencia = Inicio: ${intervRef.prmDat.format("dd/MM/yyyy")} Fim: ${intervRef.ultDat.format("dd/MM/yyyy")}"

            Rh.list().each{r->
                log.debug "Programa: $r.nome"
                r.unidades.each{u->
                    log.debug "Unidade: $u.nome"
                    u.funcionarios.each {f->
                        if(r.taxaUtilizacao) cobrarTaxaUtilizacao(f,intervRef,r.taxaUtilizacao)
                        if(r.taxaMensalidade) cobrarMensalidade(f,intervRef,r.taxaMensalidade)
                        if(r.taxaEmissaoCartao) cobrarEmissaoCartao(f,intervRef,r.taxaEmissaoCartao)
                        if(r.taxaReemissaoCartao) cobrarReemissaoCartao(f,intervRef,r.taxaReemissaoCartao)
                    }
                }
            }
        }else throw new RuntimeException("Data de Referencia nao definida!")

        log.debug "Cobranca de Taxas Cartoes finalizada!"
    }
}
