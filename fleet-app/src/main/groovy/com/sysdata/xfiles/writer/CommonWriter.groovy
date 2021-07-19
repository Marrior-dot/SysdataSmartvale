package com.sysdata.xfiles.writer

import com.sysdata.xfiles.LineFeed
import com.sysdata.xfiles.SpecRecord

abstract class CommonWriter {

    File file
    List<SpecRecord> specRecordList
    StringBuilder buffer = new StringBuilder()
    LineFeed lineFeed

    abstract def writeField(Object rawFieldValue, Map specField)


    def writeRecord(String recId, Object obj) {

        // Limpa o buffer por linha
        this.buffer.setLength(0)

        SpecRecord spec = this.specRecordList.find { it.id == recId}

        if (! spec) throw new RuntimeException("Spec de Registro #$recId nao encontrada na definicao")

        spec.fields.eachWithIndex { fld, idx ->

            def fieldValue = ""

            // Se campo tem setagem na configuração de valor de campo (atributo value)
            if (fld.containsKey("value")) {
                if (fld.value instanceof Closure) fieldValue = fld.value.call(obj)
                else fieldValue = fld.value

                // Else
            } else {
                // Objeto passado é um Map
                if (obj instanceof Map) {
                    if (! obj.containsKey(fld.name)) throw new RuntimeException("Map de valores passado não possui valor para o atributo (${fld.name}) do registro ($spec.name)")
                    fieldValue = obj."$fld.name"
                }
            }

            writeField(fieldValue, fld)
        }
        this.file << buffer.toString() + lineFeed.chars

        println ""
    }

}