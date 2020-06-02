//Transacao.count()

import com.sysdata.gestaofrota.MaquinaMotorizada
import com.sysdata.gestaofrota.PortadorMaquina
import com.sysdata.gestaofrota.StatusControleAutorizacao
import com.sysdata.gestaofrota.StatusTransacao
import com.sysdata.gestaofrota.TipoCombustivel
import com.sysdata.gestaofrota.TipoTransacao
import com.sysdata.gestaofrota.Veiculo


Cartao crt=Cartao.findByNumero("6234090600000022")
Estabelecimento estab=Estabelecimento.findByCodigo("000000000000002")


Veiculo veiculo=((crt.portador as PortadorMaquina).maquina as Veiculo)


def trn=new Transacao()
trn.with{
    valor=10.00
    dataHora=new Date()
    status= StatusTransacao.AGENDAR
    nsu=1
    numeroCartao=crt.numero
    codigoEstabelecimento=estab.codigo
    terminal="10109040"
    nsuTerminal=1
    codigoRetorno="00"
    placa=veiculo.placa
    statusControle=StatusControleAutorizacao.CONFIRMADA
    tipo=TipoTransacao.COMBUSTIVEL
    cartao=crt
    estabelecimento=estab
    combustivel=TipoCombustivel.GASOLINA
    quilometragem=1
    precoUnitario=1
    maquina=veiculo
}

trn.save flush:true