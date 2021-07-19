package com.sysdata.gestaofrota.propriedades

import com.sysdata.gestaofrota.Propriedade

/**
 *

builder.property {
    propertyGroup(name: "Parâmetro Autorizador") {
        prop(name: 'intervaloTempo', mandatory: true, label: "Intervalo de tempo entre Abastecimentos (Horas)", dataType: "integer")
        prop(name: 'intervaloPercorrido', mandatory: true, label: "Intervalo percorrido entre Abastecimentos (KM)", dataType: "integer")
        prop(name: 'horaAbastecimentoInicio', mandatory: true, label: "Abastecimento começa às", dataType: "integer")
        prop(name: 'horaAbastecimentoFim', mandatory: true, label: "Abastecimento termina às", dataType: "integer")
        prop(name: 'qtdeMaximaAbastecimentoDia', mandatory: true, label: "Qtde Máxima Abastecimento por dia (lt)", dataType: "integer")
        prop(name: 'precoMaximoGasolina', mandatory: true, label: "Preço Máximo Gasolina", dataType: "integer")
        prop(name: 'precoMaximoAlcool', mandatory: true, label: "Preço Máximo Alcool", dataType: "integer")
        prop(name: 'precoMaximoDiesel', mandatory: true, label: "Preço Máximo Alcool", dataType: "integer")
        prop(name: 'precoMaximoGasolinaAditivada', mandatory: true, label: "Preço Máximo Gasolina Aditivada", dataType: "integer")
        prop(name: 'diaSemanaInicio', mandatory: true, label: "Semana começa em:", dataType: "integer")
        prop(name: 'diaSemanaFim', mandatory: true, label: "Semana termina em:", dataType: "integer")
    }
}

 */

class Configuration {
    List propertyGroups = []
}


class PropertyGroup {
    String name
    List textProps = []
    List selectProps = []

    byte countPropsInline(byte numRow) {
        return textProps.count{ it.row == numRow } + selectProps.count{ it.row == numRow }
    }

    List getProps() {
        return textProps + selectProps
    }
}

abstract class Prop {
    byte order
    String name
    boolean mandatory
    String label
    def dataType
    byte row

    abstract String render(Propriedade propriedade, g)
}

class TextProp extends Prop {

    @Override
    String render(Propriedade propriedade, g) {
        return  "${g.textField(name: "propriedades[${this.order}].valor", class: 'form-control editable', value: "${propriedade ? propriedade.valorConvertido : ''}")}"
    }
}


class SelectProp extends Prop {
    Map options

    @Override
    String render(Propriedade propriedade, g) {
        return  """${
            g.select(
                name: "propriedades[${this.order}].valor",
                class: 'form-control editable',
                value: "${propriedade ? propriedade.valorConvertido : ''}",
                from: this.options,
                optionKey: 'key',
                optionValue: 'value'
            )}"""
    }
}
