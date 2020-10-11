package com.sysdata.xfiles.writer

import com.sysdata.xfiles.FieldDataType


class FixedSizeWriter extends CommonWriter {

    @Override
    def writeField(Object rawFieldValue, Map specField) {

        println "\t$specField.name: $rawFieldValue"

        if (!specField.containsKey("size"))
            throw new RuntimeException("Campo ($specField.name) não possui propriedade (size) definida!")

        if (!rawFieldValue && specField.containsKey("mandatory") && specField.mandatory)
            throw new RuntimeException("Campo ($specField.name) é obrigatório. Valor não pode ser nulo!")

        def fieldValue = rawFieldValue
        switch (specField.datatype) {

            // Quando campo do tipo INTEIRO preencher com 0's a esquerda para completar tamanho
            case [FieldDataType.INTEGER, FieldDataType.LONG, FieldDataType.BYTE]:

                if (rawFieldValue instanceof Integer || rawFieldValue instanceof Long || rawFieldValue instanceof Byte)
                    fieldValue = sprintf("%0${specField.size}d", rawFieldValue)
                else
                    throw new RuntimeException("Valor ($rawFieldValue) do campo ($specField.name) não é do tipo especificado!!!")
                break

            case [FieldDataType.FLOAT, FieldDataType.DOUBLE, FieldDataType.BIGDECIMAL]:
                throw new RuntimeException("Tratamento para valores fracionários ainda não implementado :P !!!")

            case FieldDataType.DATE_TIME:
                if (! specField.format)
                    throw new RuntimeException("Campo data/hora (${specField.name}) sem formatação definida!")
                fieldValue = (rawFieldValue as Date).format(specField.format)
                break

            // Considerar alinhamento a esquerda como padrão
            default:
/*
                if (!rawFieldValue && (!specField.containsKey("mandatory") || specField.mandatory == false))
                    rawFieldValue = ""
*/

                if (rawFieldValue.length() > specField.size)
                    rawFieldValue = rawFieldValue.substring(0, specField.size)
                fieldValue = sprintf("%-${specField.size}s", rawFieldValue)
                break
        }

        this.buffer.append(fieldValue)


    }
}