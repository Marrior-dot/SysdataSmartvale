package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class MockTransacaoService {

    private Random random = new Random()

    private List<Date> todasDatasDoIntervalo(Date di, Date df) {
        List<Date> datas = []
        Date dataReferencia = di.clone()
        while (dataReferencia <= df) {
            datas << dataReferencia
            dataReferencia += 1
        }
        datas
    }

    def gerarTransacoes(Map params) {

        def ret = [success: true, messages: []]

        if (! params.quantidade) {
            ret.success = false
            ret.messages << "Quantidade de transações não informada"
        }
        if (! params.dataInicio) {
            ret.success = false
            ret.messages << "Data inicial não informada"
        }
        if (! params.dataFim) {
            ret.success = false
            ret.messages << "Data final não informada"
        }

        if (! ret.success)
            return ret

        int qtde = params.quantidade as int
        Date dataInicio = params.date("dataInicio", "dd/MM/yyyy")
        Date dataFim = params.date("dataFim", "dd/MM/yyyy")

        if (dataInicio > dataFim) {
            ret.success = false
            ret.messages << "Data inicial superior a Data final"
            return ret
        }

        def datas = todasDatasDoIntervalo(dataInicio, dataFim)

        def cartoesIds = Cartao.withCriteria {
                                        projections {
                                            property "id"
                                        }
                                        portador {
                                            gt("saldoTotal", 0.0)
                                        }
                                        'in'("status", [StatusCartao.EMBOSSING, StatusCartao.ATIVO])
                                        maxResults(qtde)
                                    }

        if (! cartoesIds) {
            ret.success = false
            ret.messages << "Não há cartões com saldo disponível"
            return ret
        }

        def estabsIds = Estabelecimento.withCriteria {
                            projections {
                                property "id"
                            }
                            eq("status", Status.ATIVO)
                            maxResults(qtde)
                        }

        if (! estabsIds) {
            ret.success = false
            ret.messages << "Não há ECs ativos"
            return ret
        }

        qtde.times { i ->
            def cid = cartoesIds[Math.abs(random.nextInt() % cartoesIds.size())]
            Cartao crt = Cartao.get(cid)

            def saldoDisp = crt.saldoTotal
            def intValor = (int) saldoDisp - 1

            def ints = Math.abs(random.nextInt() % intValor)
            def decs = Math.abs(random.nextInt() % 99)

            def valorTrn = (ints + (decs / 100).round(2))

            def eid = estabsIds[Math.abs(random.nextInt() % estabsIds.size())]
            Estabelecimento estab = Estabelecimento.get(eid)

            MaquinaMotorizada maquinaMotorizada
            Funcionario funcionario

            if (crt.portador.instanceOf(PortadorFuncionario)) {
                funcionario = (crt.portador as PortadorFuncionario).funcionario
                if (! funcionario.veiculos)
                    throw new RuntimeException("Funcionário ($funcionario) não está relacionado a nenhum veículo!")
                def maqList = funcionario.veiculos as List
                maquinaMotorizada = maqList[Math.abs(random.nextInt() % maqList.size())].maquina
            } else if (crt.portador.instanceOf(PortadorMaquina)) {
                maquinaMotorizada = (crt.portador as PortadorMaquina).maquina
                def funcList = maquinaMotorizada.funcionarios as List
                funcionario = funcList[Math.abs(random.nextInt() % funcList.size())]
            }

            TipoCombustivel tipoComb
            switch (maquinaMotorizada.tipoAbastecimento) {
                case [TipoAbastecimento.BICOMBUSTIVEL, TipoAbastecimento.GASOLINA]:
                    tipoComb = TipoCombustivel.GASOLINA
                    break
                case TipoAbastecimento.ALCOOL:
                    tipoComb = TipoCombustivel.ALCOOL
                    break
                case TipoAbastecimento.DIESEL:
                    tipoComb = TipoCombustivel.DIESEL
                    break
            }

            ProdutoEstabelecimento produtoEstab = ProdutoEstabelecimento
                                                                .findAllByEstabelecimento(estab)
                                                                .find { it.produto.nome == tipoComb.nome }
            if (! produtoEstab)
                throw new RuntimeException("EC #$estab.id - Combustível ($tipoComb) não vinculado")

            Date dataTransacao = datas[Math.abs(random.nextInt() % datas.size())]

            Transacao transacao = new Transacao()
            transacao.with {
                origem = OrigemTransacao.MOCK
                dataHora = dataTransacao
                valor = valorTrn
                participante = funcionario
                statusControle = StatusControleAutorizacao.CONFIRMADA
                status = StatusTransacao.AGENDAR
                nsu = i
                nsuTerminal = i
                codigoRetorno = "00"
                tipo = TipoTransacao.COMBUSTIVEL
                numeroCartao = crt.numero
                cartao = crt
                estabelecimento = estab
                codigoEstabelecimento = estab.codigo
                terminal = "10109010"
                maquina = maquinaMotorizada
                if (maquinaMotorizada.instanceOf(Veiculo)) {
                    placa = (maquinaMotorizada as Veiculo).placa
                    quilometragem = (maquinaMotorizada as Veiculo).hodometroAtualizado
                }
                else if (maquinaMotorizada.instanceOf(Equipamento))
                    codigoEquipamento = (maquinaMotorizada as Equipamento).codigo
            }

            transacao.addToProdutos(new TransacaoProduto(produto: produtoEstab.produto, precoUnitario: BigDecimal.valueOf(produtoEstab.valor)))

            if (! transacao.save(flush: true)) {
                throw new RuntimeException("Erro: " + transacao.errors)
            }
        }
        ret
    }
}
