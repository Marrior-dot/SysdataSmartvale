package com.sysdata.gestaofrota.proc.notafiscal.barueri

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.SpecRecord


class SpecArquivoRPSBarueri {

    static SpecRecord regHeader = new SpecRecord(id: "1", nome: "Registro Header",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "1"],
            [name: "inscricaoContribuinte", size: 7],
            [name: "versaoLayout", size: 6, value: "PMB002"],
            [name: "idRemessa", size: 11, datatype: FieldDataType.INTEGER ]
        ]
    )

    static SpecRecord regDetalhe= new SpecRecord(id: "2", nome: "Registro Detalhe",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "2"],
            [name: "tipoRPS", size: 5, value: "RPS"],
            [name: "serieRPS", size: 4, value: ""],
            [name: "serieNFe", size: 5, value: ""],
            [name: "numeroRPS", size: 5, datatype: FieldDataType.INTEGER],
            [name: "dataHoraRPS", datatype: FieldDataType.DATE_TIME, format: "yyyyMMddHHmmss"],
            [name: "codigoMotivoCancelamento", size: 2, value: ""],
            [name: "numeroNFeCancelada", size: 7, datatype: FieldDataType.INTEGER, value: "0"],
            [name: "serieNFeCancelada", size: 5, value: ""],
            [name: "dataEmissaoNFeCancelada", size: 8, value: ""],
            [name: "descricaoCancelamento", size: 180, value: ""],
            [name: "codigoServicoPrestado", size: 9, datatype: FieldDataType.INTEGER, value: ""],
            [name: "localPrestacaoServico", size: 1, value: "1"],
            [name: "servicoViasPublicas", size: 1, value: "2"],
            [name: "enderecoServicoPrestado", size: 75],
            [name: "numeroLogradouroServicoPrestado", size: 9],
            [name: "complementoLogradouroServicoPrestado", size: 30],
            [name: "bairroLogradouroServicoPrestado", size: 40],
            [name: "cidadeLogradouroServicoPrestado", size: 40],
            [name: "ufLogradouroServicoPrestado", size: 2],
            [name: "cepLogradouroServicoPrestado", size: 8],
            [name: "quantidadeServico", size: 6, datatype: FieldDataType.INTEGER, value: "1"],
            [name: "valorServico", size: 15, datatype: FieldDataType.LONG],
            [name: "reservado", size: 5, value: ""],
            [name: "valorTotalRetencoes", size: 15],
            [name: "tomadorEstrangeiro", size: 1, value: "2"],
            [name: "paisTomadorEstrangeiro", size: 3, datatype: FieldDataType.INTEGER, value: "0"],
            [name: "servicoPrestadoExportacao", size: 1, datatype: FieldDataType.INTEGER, value: "0"],
            [name: "indicadorCPFCNPJ", size: 1, datatype: FieldDataType.INTEGER],
            [name: "CPFCNPJTomador", size: 14, datatype: FieldDataType.INTEGER],
            [name: "razaoSocialTomador", size: 60],
            [name: "enderecoTomador", size: 75],
            [name: "numeroLogradouroTomador", size: 9],
            [name: "complementoLogradouroTomador", size: 30],
            [name: "bairroLogradouroTomador", size: 40],
            [name: "cidadeLogradouroTomador", size: 40],
            [name: "ufLogradouroTomador", size: 2],
            [name: "cepLogradouroTomador", size: 8],
            [name: "emailTomador", size: 8],
            [name: "numeroFatura", size: 6, datatype: FieldDataType.INTEGER],
            [name: "valorFatura", size: 15, datatype: FieldDataType.LONG],
            [name: "formaPagamento", size: 15],
            [name: "descricaoServico", size: 1000],
        ]
    )

    static SpecRecord regTrailer = new SpecRecord(id: "9", nome: "Registro Trailler",
            fieldsId: ["tipoRegistro"],
            fields: [
                [name: "tipoRegistro", size: 1, value: "9"],
                [name: "numeroTotalLinhasArquivo", size: 7, datatype: FieldDataType.INTEGER],
                [name: "valorTotalServicos", size: 15, datatype: FieldDataType.LONG],
                [name: "valorTotalServicos", size: 15, datatype: FieldDataType.LONG],
            ]
    )


}