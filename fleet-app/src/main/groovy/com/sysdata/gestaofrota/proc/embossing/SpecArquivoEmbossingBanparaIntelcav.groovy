package com.sysdata.gestaofrota.proc.embossing

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.SpecRecord


class SpecArquivoEmbossingBanparaIntelcav {

    static final SpecRecord regHeader = new SpecRecord(id: "H", name: "Header do Arquivo",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "H"],
            [name: "banco", size: 3, value: "037"],
            [name: "produto", size: 1, value: "7"],
            [name: "subProduto", size: 1, value: "4"],
            [name: "data", size: 8, datatype: FieldDataType.DATE_TIME, format: "yyyyMMdd"],
            [name: "numeroPedido", size: 4, datatype: FieldDataType.LONG]
        ]
    )

    static final SpecRecord regCombustivel = new SpecRecord(id: "D", name: "Registro de Cartão",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "D"],
            [name: "sequenciaCartao", size: 6],
            [name: "indic-primeira-linha", size: 1, value: ">"],
            [name: "empresa", size: 1, value: ">"],
            [name: "filler1", size: 1, value: ""],
            [name: "orgao", size: 14],
            [name: "indic-segunda-linha", size: 1, value: ">"],
            [name: "renavam", size: 9, datatype: FieldDataType.INTEGER, value: 0],
            [name: "filler2", size: 2, value: ""],
            [name: "placa", size: 10],
            [name: "indic-terceira-linha", size: 1, value: ">"],
            [name: "marca", size: 10],
            [name: "filler3", size: 1, value: ""],
            [name: "modelo", size: 10],
            [name: "indic-quarta-linha", size: 1, value: ">"],
            [name: "combustivel", size: 10],
            [name: "filler4", size: 1, value: ""],
            [name: "filler5", size: 3, value: "VIA"],
            [name: "filler6", size: 1, value: ""],
            [name: "viaCartao1", size: 1, datatype: FieldDataType.INTEGER],
            [name: "filler7", size: 1, value: ";"],
            [name: "filler8", size: 1, value: "53"],
            [name: "banco", size: 3, value: "037"],
            [name: "agencia", size: 4, datatype: FieldDataType.INTEGER, value: 0],
            [name: "conta", size: 11, datatype: FieldDataType.INTEGER, value: 0],
            [name: "filler9", size: 1, value: "="],
            [name: "reservado", size: 1, value: "0"],
            [name: "titularidade", size: 1, value: "9"],
            [name: "filler10", size: 1, value: "8"],
            [name: "anoValidade", size: 2, datatype: FieldDataType.INTEGER],
            [name: "mesValidade", size: 2, datatype: FieldDataType.INTEGER],
            [name: "produto", size: 1, value: "7"],
            [name: "subProduto", size: 1, value: "4"],
            [name: "versaoTrilha", size: 1, value: "1"],
            [name: "senhaCartao", size: 16],
            [name: "viaCartao2", size: 1, datatype: FieldDataType.INTEGER],
            [name: "filler10", size: 3, value: "? _"],
        ]
    )
}