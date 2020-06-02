package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.SpecRecord
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class ProcessaArquivoPaysmartService {

    GrailsApplication grailsApplication

    SpecRecord regHeader = new SpecRecord(name: "Header Arquivo",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "H"],
            [name: "sequencial", size: 8, value: "00000001"],
            [name: "versao", size: 2, value: "09"],
            [name: "nomeEmpresa", size: 16, value: "SYSDATA"],
            [name: "separador1", size: 4, value: "BIN="],
            [name: "bin", size: 6, value: grailsApplication.config.projeto.administradora.bin],
            [name: "produto", size: 34, value: grailsApplication.config.projeto.embossing.produto],
            [name: "separador2", size: 5, value: "DATE="],
            [name: "data", size: 8, datatype: FieldDataType.DATE_TIME, format: "yyyyMMdd"],
            [name: "separador3", size: 5, value: "FILE SEQUENCE="],
            [name: "fileSequence", size: 5, datatype: FieldDataType.INTEGER],
            [name: "separador4", size: 5, value: "MODE="],
            [name: "modo", size: 4],
            [name: "separador5", size: 12, value: "NUM RECORDS="],
            [name: "quantidade", size: 8, datatype: FieldDataType.INTEGER],
            [name: "separador6", size: 4, value: "NSA="],
            [name: "nsa", size: 5, datatype: FieldDataType.INTEGER],
            [name: "rfu", size: 58, value: ""],
        ]
    )

    SpecRecord regEmbossing = new SpecRecord(name: "Registro Embossing",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "D"],
            [name: "sequencial", size: 8, datatype: FieldDataType.INTEGER],
            [name: "rfu1", size: 7, value: ""],
            [name: "produto", size: 2, value: "20"],
            [name: "agencia", size: 4, value: ""],
            [name: "posto", size: 2, value: ""],
            [name: "conta", size: 10, value: ""],
            [name: "fixo1", size: 1, value: "\$"],
            [name: "emb1_PAN", size: 20],
            [name: "fixo2", size: 1, value: "*"],
            [name: "emb2_ExpDate", size: 24],
            [name: "fixo3", size: 1, value: "*"],
            [name: "emb3_CardholderName", size: 24],
            [name: "fixo4", size: 24, value: "*"],
            [name: "emb4", size: 24],
            [name: "fixo5", size: 6, value: "*     "],
            [name: "fixo6", size: 1, value: "%"],
            [name: "trilha1", size: 65],
            [name: "fixo7", size: 1, value: "?"],
            [name: "fixo8", size: 1, value: ";"],
            [name: "trilha2", size: 37],
            [name: "fixo9", size: 1, value: "?"],
            [name: "fixo10", size: 1, value: "|"],
            [name: "dadosPostagem", size: 168],
            [name: "cvv2", size: 3],
            [name: "fixo11", size: 4, value: ""],
            [name: "cpf", size: 14, value: ""],
            [name: "fixo12", size: 4, value: ""],
            [name: "cnpj", size: 15, value: ""],
            [name: "fixo13", size: 4, value: ""],
            [name: "celular", size: 15, value: ""],
            [name: "fixo14", size: 4, value: ""],
            [name: "dataEfetivacao", size: 6, value: ""],
            [name: "dataNascPortador", size: 10, value: ""],
            [name: "rfu2", size: 95, value: ""],
            [name: "titularidade", size: 2, value: ""],
            [name: "via", size: 2, value: ""],
            [name: "aplicacoes", size: 2, value: ""],

        ]
    )


    def serviceMethod() {

    }
}
