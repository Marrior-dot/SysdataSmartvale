package com.sysdata.xfiles

import com.sysdata.xfiles.writer.CommonWriter
import com.sysdata.xfiles.writer.FixedSizeWriter
import com.sysdata.xfiles.writer.SeparatorWriter


class WriterProcessor extends FileProcessor {

    private static CommonWriter writerProcessor

    static void initProcessor() {

        File.metaClass.withSeparatorWriter = { LineFeed lineFeed = LineFeed.LINUX, String sep, List<SpecRecord> specs, Closure clos ->

            //TODO: Melhorar lista de caracteres inválidos para separador
            if (! sep) throw new RuntimeException("Separador nao pode ser NULO!")
            if (sep in ['.',',','/','']) throw new RuntimeException("Caracter invalido ($sep) para geracao de arquivo")

            writerProcessor = new SeparatorWriter(file: delegate, specRecordList: specs, separator: sep)
            clos(writerProcessor)
        }

        File.metaClass.withFixedSizeWriter = { LineFeed lineFeed = LineFeed.LINUX, List<SpecRecord> specs, Closure clos ->
            writerProcessor = new FixedSizeWriter(file: delegate, specRecordList: specs, lineFeed: lineFeed)
            clos.call(writerProcessor)
        }

    }


}