package com.sysdata.gestaofrota

import grails.databinding.BindUsing

abstract class Portador {
    Conta conta = new Conta()

    @BindUsing({obj, source ->
        Util.parseCurrency(source['limiteTotal'])
    })
    BigDecimal limiteTotal = 0D
    BigDecimal limiteDiario
    BigDecimal limiteMensal
    BigDecimal limiteCredito
    BigDecimal saldoTotal = 0D
    BigDecimal saldoDiario
    BigDecimal saldoMensal
    Status status = Status.ATIVO
    Boolean vincularCartao = true

    static hasMany = [cartoes: Cartao, relacaoCartao: RelacaoCartaoPortador]

    static belongsTo = [unidade: Unidade]

    static constraints = {
        limiteDiario nullable: true
        limiteMensal nullable: true
        limiteCredito nullable: true
        saldoDiario nullable: true
        saldoMensal nullable: true
        status nullable: true
        unidade nullable: true
        conta nullable: true
        vincularCartao nullable: true

        limiteTotal validator: { val, obj ->
            if (! obj.instanceOf(PortadorAnonimo)) {
                // INSERT
                if (! obj.id) {
                    def rhLimite = obj.unidade.rh.limiteTotal
                    def rhLimiteComprometido = obj.unidade.rh.limiteComprometido

                    if (obj.limiteTotal + rhLimiteComprometido > rhLimite)
                        return ["portador.limiteTotal.superiorAoComprometido"]
                    // UPDATE
                } else {
                    def oldValue = obj.getPersistentValue('limiteTotal')
                    def newValue = val

                    if (newValue > oldValue) {
                        def dif = newValue - oldValue
                        def rhLimite = obj.unidade.rh.limiteTotal
                        def rhLimiteComprometido = obj.unidade.rh.limiteComprometido
                        if (rhLimiteComprometido + dif > rhLimite)
                            return ["portador.limiteTotal.superiorAoComprometido"]
                    }
                }
            }
        }
    }

    static transients = ['cartaoAtivo', 'cartaoAtual', 'saldo', 'nomeEmbossing', 'endereco', 'cpfFormatado', 'cnpj', 'telefone', 'ativo']


    Boolean getAtivo() {

        if (this.instanceOf(PortadorFuncionario)) {
            PortadorFuncionario portFunc = this as PortadorFuncionario
            return portFunc.funcionario.status == Status.ATIVO
        } else if (this.instanceOf(PortadorMaquina)) {
            PortadorMaquina portMaq = this as PortadorMaquina
            return portMaq.maquina.status == Status.ATIVO
        }
    }

    Cartao getCartaoAtivo() {
        //cartoes.find { it.status == StatusCartao.ATIVO || it.status == StatusCartao.EMBOSSING }
        return cartoes.find { it.status == StatusCartao.ATIVO }
    }

    Cartao getCartaoAtual() {
        return cartoes.max { it.dateCreated }
    }

    BigDecimal getSaldo() {
        conta.saldo
    }

    String getNomeEmbossing() {
        if (this.instanceOf(PortadorFuncionario))
            return (this as PortadorFuncionario)?.funcionario?.nomeEmbossing
        else if (this.instanceOf(PortadorMaquina)) {
            MaquinaMotorizada maquina = (this as PortadorMaquina)?.maquina
            return maquina?.nomeEmbossing
        }
        return "<< Sem portador vinculado >>"
    }

    Endereco getEndereco() {
        if (this.instanceOf(PortadorFuncionario))
            return (this as PortadorFuncionario)?.funcionario?.endereco
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

}
