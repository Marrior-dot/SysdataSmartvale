package com.sysdata.gestaofrota.proc.faturamento.cobranca

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.SpecRecord

class SpecArquivoRemessaBancoBrasil {

    static final SpecRecord regHeader = new SpecRecord(id: "0", name: "Registro Header",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "0"],
            [name: "tipoOperacao", size: 1, value: "1"],
            [name: "identificacaoTipoOperacao", size: 7],
            [name: "tipoServico", size: 2, value: "01"],
            [name: "identificacaoTipoServico", size: 8, value: "COBRANCA"],
            [name: "brancos1", size: 7, value: ""],
            [name: "agenciaBeneficiario", size: 4, datatype: FieldDataType.INTEGER],
            [name: "dvAgencia", size: 1],
            [name: "contaCorrenteBeneficiario", size: 8, datatype: FieldDataType.INTEGER],
            [name: "dvContaCorrente", size: 1],
            [name: "complemento", size: 6, value: "000000"],
            [name: "nomeBeneficiario", size: 30],
            [name: "usoBanco", size: 18,  value: "001BANCODOBRASIL"],
            [name: "dataGravacao", size: 6, datatype: FieldDataType.DATE_TIME, format: "ddMMyy"],
            [name: "sequencialRemessa", size: 7, datatype: FieldDataType.INTEGER],
            [name: "brancos2", size: 22, value: ""],
            [name: "convenio", size: 7, datatype: FieldDataType.INTEGER],
            [name: "brancos3", size: 258, value: ""],
            [name: "sequencialRegistro", size: 6, value: "000001"]
        ]
    )

    static final SpecRecord regDetalhe = new SpecRecord(id: "7", name: "Registro Detalhe",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "7"],
            [name: "tipoInscricao", size: 2, value: "02"],
            [name: "cpfCnpj", size: 14, datatype: FieldDataType.LONG],
            [name: "prefixoAgencia", size: 4, datatype: FieldDataType.INTEGER],
            [name: "dvPrefixoAgencia", size: 1],
            [name: "contaCorrenteBeneficiario", size: 8, datatype: FieldDataType.INTEGER],
            [name: "dvContaCorrente", size: 1],
            [name: "convenioBeneficiario", size: 7, datatype: FieldDataType.INTEGER],
            [name: "codigoControleEmpresa", size: 25, value: ""],
            [name: "nossoNumero", size: 17, datatype: FieldDataType.LONG],
            [name: "numeroPrestacao", size: 2, value: "00"],
            [name: "grupoValor", size: 2, value: "00"],
            [name: "tipoMoeda", size: 2, value: ""],
            [name: "brancos1", size: 1, value: ""],
            [name: "indicativoMensagem", size: 1, value: ""],
            [name: "brancos2", size: 3, value: ""],
            [name: "variacaoCarteira", size: 3, datatype: FieldDataType.INTEGER],
            [name: "contaCaucao", size: 1, value: "0"],
            [name: "numeroBordero", size: 6, datatype: FieldDataType.INTEGER, value: 0],
            [name: "tipoCobranca", size: 5, value: ""],
            [name: "carteiraCobranca", size: 2, datatype: FieldDataType.INTEGER],
            [name: "comando", size: 2, value: "01"],
            [name: "seuNumeroNumeroTitulo", size: 10],
            [name: "dataVencimento", size: 6, datatype: FieldDataType.DATE_TIME, format: "ddMMyy"],
            [name: "valorTitulo", size: 13, datatype: FieldDataType.LONG],
            [name: "numeroBanco", size: 3, value: "001"],
            [name: "prefixoAgenciaCobradora", size: 4, value: "0000"],
            [name: "dvPrefixoAgenciaCobradora", size: 1, value: ""],
            [name: "especieTitulo", size: 2, datatype: FieldDataType.INTEGER],
            [name: "aceiteTitulo", size: 1],
            [name: "dataEmissao", size: 6, datatype: FieldDataType.DATE_TIME, format: "ddMMyy"],
            [name: "instrucao1", size: 2, value: "00"],
            [name: "instrucao2", size: 2, value: "00"],
            [name: "jurosDeMora", size: 13, datatype: FieldDataType.LONG],
            [name: "dataLimiteDesconto", size: 6, datatype: FieldDataType.INTEGER],
            [name: "valorDesconto", size: 13, datatype: FieldDataType.LONG],
            [name: "valorIOF", size: 13, datatype: FieldDataType.LONG],
            [name: "valorAbatimento", size: 13, datatype: FieldDataType.LONG],
            [name: "tipoInscricaoPagador", size: 2, value: "02"],
            [name: "cpfCnpjPagador", size: 14, datatype: FieldDataType.LONG],
            [name: "nomePagador", size: 37],
            [name: "brancos3", size: 3, value: ""],
            [name: "enderecoPagador", size: 40],
            [name: "bairroPagador", size: 12],
            [name: "cepPagador", size: 8],
            [name: "cidadePagador", size: 15],
            [name: "ufPagador", size: 2],
            [name: "observacoesMensagensAvalista", size: 40, value: ""],
            [name: "diasProtestoNegativacao", size: 2, value: ""],
            [name: "indicadorRecebimentoParcial", size: 1, value: "N"],
            [name: "sequencialRegistro", size: 6, datatype: FieldDataType.INTEGER]
        ]
    )

    static final SpecRecord regTrailer = new SpecRecord(id: "9", name: "Registro Trailer",
        fieldsId: ["tipoRegistro"],
        fields: [
            [name: "tipoRegistro", size: 1, value: "9"],
            [name: "brancos", size: 393, value: ""],
            [name: "sequencialRegistro", size: 6, datatype: FieldDataType.INTEGER]
        ]
    )

}