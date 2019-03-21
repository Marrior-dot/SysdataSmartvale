package com.sysdata.gestaofrota

class Transacao {

    BigDecimal valor
    Date dateCreated
    Date dataHora
    StatusTransacao status
    Integer nsu
    String numeroCartao
    String codigoEstabelecimento
    String terminal
    Integer nsuTerminal
    String codigoRetorno
    String placa
    StatusControleAutorizacao statusControle
    TipoTransacao tipo
    Cartao cartao
    Estabelecimento estabelecimento
    Boolean autorizada
    TipoCombustivel combustivel
    Long quilometragem
    BigDecimal precoUnitario
    MotivoNegacao motivoNegacao
    MaquinaMotorizada maquina
    String codigoEquipamento
    BigDecimal taxaAdm
    BigDecimal valorReembolso

    static belongsTo = [participante: Participante]

    static hasMany = [lancamentos: Lancamento, produtos: TransacaoProduto]

    static transients = ['estornavel']

    static constraints = {
        dataHora(nullable: true)
        placa(nullable: true)
        nsu(nullable: true)
        numeroCartao(nullable: true)
        codigoEstabelecimento(nullable: true)
        nsuTerminal(nullable: true)
        codigoRetorno(nullable: true)
        participante(nullable: true)
        estabelecimento(nullable: true)
        cartao(nullable: true)
        terminal(nullable: true)
        autorizada(nullable: true)
        combustivel(nullable: true)
        quilometragem(nullable: true)
        precoUnitario(nullable: true)
        statusControle(nullable: true)
        motivoNegacao(nullable: true)
        maquina nullable: true
        codigoEquipamento nullable: true
        taxaAdm nullable: true
        valorReembolso nullable: true
    }


    Conta getConta() {
        this.cartao?.funcionario?.conta
    }


    static mapping = {
        id generator: 'sequence', params: [sequence: 'transacao_seq']
        version false
    }

    static hibernateFilters = {
        // Filtros Transacao
        transacaoPorEstabelecimento(condition: 'estabelecimento_id=:estabelecimento_id', types: 'long')

        transacaoPorParticipante(condition: 'participante_id=:participante_id', types: 'long')

        transacaoPorRH(condition: 'participante_id in (select f.id from Participante f where f.unidade_id in (select u.id from Unidade u where u.rh_id=:rh_id))', types: 'long')

        transacaoPorPosto(condition: 'estabelecimento_id in (select e.id from Participante e where e.empresa_id=:posto_id)', types: 'long')
    }

    static def valorMensal(Map map, String tipoValorMensal) {
        def query = new StringBuilder()
        def params = []
        query << """
           select sum(t.valor), $tipoValorMensal(t.dataHora) from Transacao t
       """
        if (!map.isEmpty())
            query << " where 1 = 1 "
        if (map.mes) {
            query << " and MONTH(t.dataHora) = ?"
            params << map.mes
        }
        if (map.ano) {
            query << " and YEAR(t.dataHora) = ?"
            params << map.ano
        }
        if (map.status) {
            query << " and statusControle = ?"
            params << map.status
        }
        if (map.tipo) {
            query << " and tipo = ?"
            params << map.tipo
        }
        if (map.estabelecimento) {
            query << ' and t.estabelecimento = ?'
            params << map.estabelecimento
        }
        query << " group by $tipoValorMensal(t.dataHora)"
        Transacao.executeQuery(query.toString(), params)
    }

    boolean getEstornavel() {
        return status in [StatusTransacao.AGENDAR, StatusTransacao.AGENDADA] &&
                tipo in [TipoTransacao.COMBUSTIVEL, TipoTransacao.SERVICOS] &&
                statusControle == StatusControleAutorizacao.CONFIRMADA
    }
}
