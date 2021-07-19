package com.sysdata.xfiles.reader

import com.sysdata.xfiles.FileProcessorException
import com.sysdata.xfiles.SpecRecord
import groovy.util.logging.Slf4j

@Slf4j
class FixedSizeReader extends CommonReader {

    SpecRecord findSpecRecord(String line) {
        this.specRecordList.find {
            def match = false
            // Percorre os campos ID
            it.fieldsId.find { fid ->
                match = false
                def offset = 0
                def i = 0
                // Percorre os campos, checando se o seus
                for (int x = i; x < it.fields.size(); x++) {
                    if (fid == it.fields[x].name && it.fields[x].value == line[offset..(offset + it.fields[x].size - 1)]) {
                        match = true
                        break
                    }
                    offset = offset + it.fields[x].size
                }
                if (! match) return false
            }
            return match
        }
    }

    Map processLine(String line, Closure clos) {
        def offset = 0
        def fieldsError = [:]
        def fieldList = this.specRecord.fields
        for (field in fieldList) {
            def endPos = offset + field.size - 1
            if (line.length() <= endPos)
                throw new FileProcessorException("L:($counter) - C:$field.name -> Tamanho da linha (${line.length()}) inferior a posição final do campo ($endPos)!")

            def rawValue = line[offset..endPos].trim()
            //log.debug "${field.name}[$offset..$endPos]: ${rawValue}"

            def ret = handleRawValue(rawValue, field, fieldsError)
            if (! ret) return fieldsError
            offset = endPos + 1
        }
        fieldsError
    }

}