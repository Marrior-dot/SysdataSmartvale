package com.sysdata.gestaofrota.proc.reembolso

import com.sysdata.gestaofrota.Cartao
import com.sysdata.gestaofrota.Equipamento
import com.sysdata.gestaofrota.Estabelecimento
import com.sysdata.gestaofrota.Funcionario
import com.sysdata.gestaofrota.MaquinaFuncionario
import com.sysdata.gestaofrota.OrigemTransacao
import com.sysdata.gestaofrota.PortadorFuncionario
import com.sysdata.gestaofrota.PortadorMaquina
import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.StatusCartao
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.StatusTransacao
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Transacao
import com.sysdata.gestaofrota.Veiculo
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import spock.lang.Specification

@Integration
@Rollback
class CorteReembolsoEstabsServiceSpec extends Specification {


    private criarTransacaoAbastecimento() {

        Estabelecimento estab = Estabelecimento.get(31)
        Cartao crt = Cartao.findByStatus(StatusCartao.ATIVO)
        Funcionario funcionario
        Veiculo veiculo
        if (crt.portador.instanceOf(PortadorFuncionario)) {
            funcionario = (crt.portador as PortadorFuncionario).funcionario
            MaquinaFuncionario veiculoFuncionario = (funcionario.veiculos as List)[0]
            veiculo = veiculoFuncionario.maquina
        }
        else if (crt.portador.instanceOf(PortadorMaquina)) {
            veiculo = (crt.portador as PortadorMaquina).maquina
            MaquinaFuncionario funcionarioVeiculo = (veiculo.funcionarios as List)[0]
            funcionario = funcionarioVeiculo.funcionario
        }

        Transacao transacao = new Transacao()
        transacao.with {
            origem = OrigemTransacao.MOCK
            dataHora = dataTransacao
            valor = 100.00
            participante = funcionario
            statusControle = StatusControleAutorizacao.CONFIRMADA
            status = StatusTransacao.AGENDAR
            nsu = 1
            nsuTerminal = 1
            codigoRetorno = "00"
            tipo = TipoTransacao.COMBUSTIVEL
            numeroCartao = crt.numero
            cartao = crt
            estabelecimento = estab
            codigoEstabelecimento = estab.codigo
            terminal = "10109010"
            maquina = veiculo
            placa = veiculo.placa
            quilometragem = veiculo.hodometroAtualizado
        }

    }


    def setup() {



    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }


}
