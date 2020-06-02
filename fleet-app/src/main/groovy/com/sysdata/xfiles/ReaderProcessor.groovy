package com.sysdata.xfiles

import com.sysdata.xfiles.reader.FixedSizeReader
import com.sysdata.xfiles.reader.SeparatorReader

class ReaderProcessor extends FileProcessor {

    static void initProcessor() {

        // Reader para colunas de tamanho fixo
        File.metaClass.withFixedSizeReader = { Map params, Closure clos ->

            if (! params.containsKey("specs")) throw new RuntimeException("Parâmetro Spec obrigatório!")
            FixedSizeReader fsReader = new FixedSizeReader(file: delegate, specRecordList: params.specs)
            fsReader.processFile(clos)
            fsReader
        }

        // Reader para colunas com caracter separador
        File.metaClass.withSeparatorReader = { Map params, Closure clos ->

            if (! params.containsKey("specs")) throw new RuntimeException("Parâmetro Spec obrigatório!")
            if (! params.containsKey("separator")) throw new RuntimeException("Parâmetro Separator obrigatório!")

            SeparatorReader sepReader = new SeparatorReader(file: delegate, specRecordList: params.specs, separator: params.separator)
            if (params.startLine) sepReader.startLine = params.startLine
            if (params.containsKey("hasRecId")) sepReader.hasRecordIdentifier =  params.hasRecId

            sepReader.processFile(clos)
            sepReader
        }

    }

}