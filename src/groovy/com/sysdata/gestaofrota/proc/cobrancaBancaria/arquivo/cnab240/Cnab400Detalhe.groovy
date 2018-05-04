package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

/**
 * Created by luiz on 15/04/18.
 */
class Cnab400Detalhe extends Cnab400Record {

    Cnab400Detalhe(){
        fields=[
                [id:"tipoRegistro",val:"1"],
                [id:"codigoInscricao"],
                [id:"numeroInscricao"],
                [id:"agencia"],
                [id:"filler1"],
                [id:"conta"],
                [id:"dac"],
                [id:"filler2"],
                [id:"instrucao",val:"0000"],
                [id:"usoEmpresa",val:"",type:DataType.ALPHA,size:25],
                [id:"nossoNumero",type:DataType.NUMERIC,size:8],
                [id:"qtdeMoeda",type:DataType.NUMERIC,size:13],
                [id:"numeroCarteira",type:DataType.NUMERIC,size:3],
                [id:"usoBanco",type:DataType.ALPHA,size:21],
                [id:"carteira"],
                [id:"codigoOcorrencia"],
                [id:"numeroDocumento",type:DataType.ALPHA,size:10],
                [id:"vencimento",type:DataType.DATE,format:"ddMMyy"],
                [id:"valorTitulo"],
                [id:"codigoBanco"],
                [id:"agenciaCobradora"],
                [id:"especie"],
                [id:"aceite"],
                [id:"dataEmissao",type:DataType.DATE,format:"ddMMyy"],
                [id:"instrucao1"],
                [id:"instrucao2"],
                [id:"juros1Dia",type:DataType.NUMERIC,size:13],
                [id:"descontoAte"],
                [id:"valorDesconto",type:DataType.NUMERIC,size:13],
                [id:"valorIOF",type:DataType.NUMERIC,size:13],
                [id:"abatimento",type:DataType.NUMERIC,size:13],
                [id:"codigoInscricao"],
                [id:"numInscPagador"],
                [id:"nomePagador",type:DataType.ALPHA,size:30],
                [id:"filler3",type:DataType.ALPHA,size:10],
                [id:"logradouroPagador",type:DataType.ALPHA,size:40],
                [id:"bairroPagador",type:DataType.ALPHA,size:12],
                [id:"cepPagador",type:DataType.NUMERIC,size:8],
                [id:"cidadePagador",type:DataType.ALPHA,size:15],
                [id:"estadoPagador"],
                [id:"sacadorAvalista",type:DataType.ALPHA,size:30],
                [id:"filler4"],
                [id:"dataMora"],
                [id:"prazoDias"],
                [id:"filler5"],
                [id:"sequencial",val:Cnab400Record.nextSequence(),size:6]
        ]

    }
}
