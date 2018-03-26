package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFactory
import com.sysdata.gestaofrota.proc.faturamento.ext.ExtensaoFaturamento
import grails.util.Holders

abstract class Portador {
    Conta conta = new Conta()
    BigDecimal limiteTotal=0D
    BigDecimal limiteDiario
    BigDecimal limiteMensal

    BigDecimal saldoTotal=0D
    BigDecimal saldoDiario
    BigDecimal saldoMensal

    static hasMany = [cartoes: Cartao]
    static belongsTo = [unidade: Unidade]

    static constraints = {
        limiteDiario nullable:true
        limiteMensal nullable:true
        saldoDiario nullable:true
        saldoMensal nullable:true
    }

    static transients = ['cartaoAtivo', 'cartaoAtual', 'saldo', 'nomeEmbossing', 'endereco', 'cpfFormatado', 'cnpj', 'telefone','ativo']


    Boolean getAtivo(){

        if(this.instanceOf(PortadorFuncionario)){
            PortadorFuncionario portFunc=this as PortadorFuncionario
            return portFunc.funcionario.status==Status.ATIVO
        }else if(this.instanceOf(PortadorMaquina)){
            PortadorMaquina portMaq=this as PortadorMaquina
            return portMaq.maquina.status==Status.ATIVO
        }
    }

    Cartao getCartaoAtivo() {
        cartoes.find { it.status == StatusCartao.ATIVO || it.status == StatusCartao.EMBOSSING }
    }

    Cartao getCartaoAtual() {
        cartoes.max { it.dateCreated }
    }

    BigDecimal getSaldo() {
        conta.saldo
    }

    String getNomeEmbossing() {
        if (this.instanceOf(PortadorFuncionario)) return (this as PortadorFuncionario)?.funcionario?.nomeEmbossing
        else if (this.instanceOf(PortadorMaquina)) {
            MaquinaMotorizada maquina = (this as PortadorMaquina)?.maquina
            return maquina?.nomeEmbossing
        }

        return ""
    }

    Endereco getEndereco() {
        if (this.instanceOf(PortadorFuncionario)) return (this as PortadorFuncionario)?.funcionario?.endereco
        return null
    }

    String getCpfFormatado() {
        String cpf = this.instanceOf(PortadorFuncionario) ? (this as PortadorFuncionario)?.funcionario?.cpf : ""
        cpf.length() > 0 ? Util.rawToCpf(cpf) : ""
    }

    String getCnpj() {
        Funcionario funcionario = this.instanceOf(PortadorFuncionario) ? (this as PortadorFuncionario)?.funcionario : null
        String cnpj = funcionario?.instanceOf(Empresa) ? (funcionario as Empresa)?.cnpj : ""

        cnpj.replaceAll('\\.', '').replaceAll('-', '')
    }

    private def initContext(Corte corte,dataProc){
        def ctx=new Expando()

        //Fecha última fatura
        ctx.ultimaFatura=this.conta.ultimaFatura
        ctx.atrasado=false

        //Data de referência para atraso pagamento
        ctx.dataRefCob=ctx.ultimaFatura?ctx.ultimaFatura.dataVencimento:dataProc

        //Inicializa (cria) objeto Fatura
        Fatura fatura=new Fatura()
        fatura.with{
            conta=this.conta
            dataVencimento=corte.dataCobranca
            data=dataProc
            corte=corte
            status=StatusFatura.ABERTA
        }


        ctx.addSaldo={tpSld,val->
            if(!ctx.novosSaldos.containsKey(tpSld)) ctx.novosSaldos[tpSld]=0.0
            ctx.novosSaldos[tpSld]+=val
        }


        ctx
    }


    Fatura faturar(Corte corte, dataProc){

        def fatConfig=Holders.grailsApplication.config.project.faturamento

        def ctx=initContext(corte,dataProc)

        //Lançamentos a FATURAR
        def lctosAFat=LancamentoPortador.withCriteria {
            eq("conta",this.conta)
            eq("statusFaturamento",StatusFaturamento.NAO_FATURADO)
            eq("corte",corte)
            order("dataEfetivacao")
        }
        Fatura fatura=ctx.fatura
        lctosAFat.each{lcto->

            ItemFatura item=lcto.faturar()
            fatura.addToItens item
            lcto.statusFaturamento=StatusFaturamento.FATURADO

            switch(lcto.tipo){
                case TipoLancamento.COMPRA:
                    if(fatConfig.controlaSaldo) ctx.addSaldo(TipoLancamento.COMPRA,lcto.valor)
                    break
                default:
                    new RuntimeException("Tipo de Lancamento (${lcto.tipo}) nao tratado no faturamento!")
            }



        }
        fatura.save()

        //Roda extensoes
        fatConfig.extensoes.each{e->
            ExtensaoFaturamento ext=ExtensaoFactory.getInstance(e)
            ext.tratar(ctx)
        }

        ultFat.status=StatusFatura.FECHADA
        ultFat.save()

        fatura
    }


}
