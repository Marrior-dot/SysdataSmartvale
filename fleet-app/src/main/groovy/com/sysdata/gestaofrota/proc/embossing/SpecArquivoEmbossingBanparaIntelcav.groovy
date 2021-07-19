package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.SpecRecord


class SpecArquivoEmbossingBanparaIntelcav {

    static final SpecRecord regHeader = new SpecRecord(id: "H", name: "Header do Arquivo",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "H"],
            [name: "sequencial", size: 6, datatype: FieldDataType.INTEGER],
            [name: "versao", size: 2, value: "10"],
            [name: "banco", size: 3, value: "037"],
            [name: "produto", size: 1, value: "7"],
            [name: "subProduto", size: 1, value: "4"],
            [name: "data", size: 8, datatype: FieldDataType.DATE_TIME, format: "yyyyMMdd"],
            [name: "numeroPedido", size: 5, datatype: FieldDataType.LONG]
        ]
    )

    static final SpecRecord regTrailer = new SpecRecord(id: "T", name: "Trailer do Arquivo",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "T"],
            [name: "sequencial", size: 6, datatype: FieldDataType.INTEGER],
            [name: "fixo", size: 6, value: "TOTAL="],
            [name: "totalCartoes", size: 6, datatype: FieldDataType.INTEGER]
        ]
    )

    static final SpecRecord regCombustivel = new SpecRecord(id: "D", name: "Registro de Cartão",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "D"],
            [name: "sequencial", size: 6, datatype: FieldDataType.INTEGER],
            [name: "fixo1", size: 1, value: "\$"],
            [name: "cartaoFormatado", size: 20],
            [name: "fixo2", size: 1, value: "*"],
            [name: "orgao", size: 14],
            [name: "placa", size: 10],
            [name: "fixo3", size: 1, value: "*"],
            [name: "marca", size: 10],
            [name: "modelo", size: 10],
            [name: "fixo4", size: 1, value: "*"],
            [name: "combustivel", size: 10],
            [name: "fixo5", size: 1, value: "%"],
            [name: "trilha1", size: 65],
            [name: "fixo6", size: 1, value: "?"],
            [name: "fixo7", size: 1, value: ";"],
            [name: "trilha2", size: 37],
            [name: "fixo8", size: 1, value: "?"],
            [name: "fixo9", size: 1, value: "|"],
            [name: "pinBlock", size: 16],
        ]
    )
}