package com.sysdata.xfiles.reader

import com.sysdata.xfiles.SpecRecord

class SeparatorReader extends CommonReader {

    String separator

    // Considera o identificador de registro como primeiro campo
    SpecRecord findSpecRecord(String line) {
        if (this.hasRecordIdentifier) {
            def recId = line[0..line.indexOf(this.separator) - 1]
            return this.specRecordList.find { it.id == recId }
        } else return this.specRecordList[0]
    }

    Map processLine(String line, Closure clos) {
        def pieces = line.split(this.separator)

        def err = [:]

        this.specRecord.fields.eachWithIndex { field, idx ->
            def i
            if (this.hasRecordIdentifier) i = idx + 1
            else i = idx

            def rawValue = pieces[i].trim()
            def ret = handleRawValue(rawValue, field, err)
            if (! ret ) return err
        }
        err
    }

}