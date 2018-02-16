package com.sysdata.gestaofrota

abstract class Portador {
    Conta conta = new Conta()
    TipoLimite tipoLimite = TipoLimite.CREDITO
    BigDecimal valorLimite = 0D


    static hasMany = [cartoes: Cartao]
    static belongsTo = [unidade: Unidade]

    static constraints = {
    }

    static transients = ['cartaoAtivo', 'cartaoAtual', 'saldo', 'nomeEmbossing', 'endereco', 'cpfFormatado', 'cnpj', 'telefone']


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




    void faturar(Corte corte, dataProc){


        def lctosAFat=LancamentoPortador.withCriteria {
            eq("conta",conta)
            eq("statusFaturamento",StatusFaturamento.NAO_FATURADO)
            eq("corte",corte)
            order("dataEfetivacao")
        }

        lctosAFat.each{lcto->
            tratarLancamento(lcto,ctx)
            lcto.statusFaturamento=StatusFaturamento.FATURADO
        }



    }


    private def initContext(Conta conta, Corte corte){
        //Contexto para guardar dados úteis para o cálculo da fatura

        def ctx=new Expando()
        ctx.conta=conta
        ctx.corte=corte
        ctx.dataFaturamento=this.dataReferencia
        ctx.fatura=new Fatura(conta:conta,status:StatusFatura.ABERTA,dataVencimento:corte.dataVencimento)

        ctx.ultimaFatura=conta.ultimaFatura
        ctx.saldoDevedor=ctx.ultimaFatura?ctx.ultimaFatura.valorTotal:0.0
        ctx.atrasado=false
        ctx.novosSaldos=[:]

        ctx.config=getConfig(conta)?.value

        //Obter fatura anterior, caso haja
        //Data de referência para calcular encargos
        ctx.dataRef=ctx.ultimaFatura?ctx.ultimaFatura.dataVencimento:this.dataReferencia

        ctx.abaterSaldo={v->
            ctx.saldoDevedor-=v
            //Se abatimentos superiores ao pagamento mínimo, entra no rotativo
            ctx.rotativo=(ctx.ultimaFatura.valorTotal-ctx.saldoDevedor)<=ctx.ultimaFatura.pagtoMinimo
        }

        ctx.getUltPagto={
            def pagList=ctx.lancamentos.findAll{it.tipo==TipoLancamento.PAGAMENTO}
            def ultPag=(pagList)?pagList[pagList.size()-1]:null
            ultPag
        }

        ctx.addSaldo={tpSld,val->
            if(!ctx.novosSaldos.containsKey(tpSld)) ctx.novosSaldos[tpSld]=0.0
            ctx.novosSaldos[tpSld]+=val
        }
        ctx
    }


    private def tratarLancamento(LancamentoPortador lcto,ctx) {

        ItemFatura item=lcto.faturar()
        ctx.fatura.addToItens item

        switch (lcto.tipo) {
        /* Compras */
            case TipoLancamento.COMPRA: case TipoLancamento.CONSOLIDACAO_FATURAS:
                if(ctx.config.controlaSaldo) ctx.addSaldo(TipoSaldo.COMPRAS,lcto.valor)
                break

        /* Pagamentos */
            case TipoLancamento.PAGAMENTO:

                /* Se pagamento depois do vencimento */
                if (!ctx.atrasado) ctx.atrasado=lcto.dataEfetivacao - ctx.dataRef > 0

                if(ctx.config.controlaSaldo) ctx.abaterSaldo(lcto.valor)
                break

            default:
                new RuntimeException("Tipo de Lancamento [${lcto.tipo}] nao definido para faturamento!!")
        }

    }
}
