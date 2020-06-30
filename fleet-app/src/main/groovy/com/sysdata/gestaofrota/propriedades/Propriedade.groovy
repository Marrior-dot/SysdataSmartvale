package com.sysdata.gestaofrota.propriedades

import com.sysdata.gestaofrota.TipoDado

/**
 *

 propriedades {

     propriedade(name: 'intervaloTempo', mandatory: true, label: "Intervalo de tempo entre Abastecimentos (Horas)", dataType: "integer")
     propriedade(name: 'intervaloPercorrido', mandatory: true, label: "Intervalo percorrido entre Abastecimentos (KM)", dataType: "integer")
     propriedade(name: 'horaAbastecimentoInicio', mandatory: true, label: "Abastecimento começa às", dataType: "integer")
     propriedade(name: 'horaAbastecimentoFim', mandatory: true, label: "Abastecimento termina às", dataType: "integer")
     propriedade(name: 'qtdeMaximaAbastecimentoDia', mandatory: true, label: "Qtde Máxima Abastecimento por dia (lt)", dataType: "integer")
     propriedade(name: 'precoMaximoGasolina', mandatory: true, label: "Preço Máximo Gasolina", dataType: "integer")
     propriedade(name: 'precoMaximoAlcool', mandatory: true, label: "Preço Máximo Alcool", dataType: "integer")
     propriedade(name: 'precoMaximoDiesel', mandatory: true, label: "Preço Máximo Alcool", dataType: "integer")
     propriedade(name: 'precoMaximoGasolinaAditivada', mandatory: true, label: "Preço Máximo Gasolina Aditivada", dataType: "integer")
     propriedade(name: 'diaSemanaInicio', mandatory: true, label: "Semana começa em:", dataType: "integer")
     propriedade(name: 'diaSemanaFim', mandatory: true, label: "Semana termina em:", dataType: "integer")

 }

 */

class Grupo {
    List propriedades = []
}


class Propriedade {

    String nome
    boolean obrigatorio
    String descricao
    def tipoDado

}