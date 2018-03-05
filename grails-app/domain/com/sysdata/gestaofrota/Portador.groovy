package com.sysdata.gestaofrota

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

    Fatura faturar(Corte corte, dataProc){

        def lctosAFat=LancamentoPortador.withCriteria {
            eq("conta",this.conta)
            eq("statusFaturamento",StatusFaturamento.NAO_FATURADO)
            eq("corte",corte)
            order("dataEfetivacao")
        }
        Fatura fatura=new Fatura()
        fatura.with{
            dataVencimento=corte.dataCobranca
            data=dataProc
            corte=corte
            status=StatusFatura.ABERTA
        }
        lctosAFat.each{lcto->
            ItemFatura item=lcto.faturar()
            fatura.addToItens item
            lcto.statusFaturamento=StatusFaturamento.FATURADO
        }
        fatura.save()
        //Fecha última fatura
        Fatura ultFat=this.conta.ultimaFatura
        ultFat.status=StatusFatura.FECHADA
        ultFat.save()

        fatura
    }


}
