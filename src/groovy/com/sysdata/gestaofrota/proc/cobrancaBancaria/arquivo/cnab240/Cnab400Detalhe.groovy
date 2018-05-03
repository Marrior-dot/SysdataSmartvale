package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

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
                [id:"filler1",val:"00"],
                [id:"conta"],
                [id:"dac"],
                [id:"filler2"],
                [id:"instrucao"],
                [id:"usoEmpresa"],
                [id:"nossoNumero"],
                [id:"qtdeMoeda"],
                [id:"numeroCarteira"],
                [id:"usoBanco"],
                [id:"carteira"],
                [id:"codigoOcorrencia"],
                [id:"numeroDocumento"],
                [id:"vencimento"],
                [id:"valorTitulo"],
                [id:"codigoBanco"],
                [id:"agenciaCobradora"],
                [id:"especie"],
                [id:"aceite"],
                [id:"dataEmissao"],
                [id:"instrucao1"],
                [id:"instrucao2"],
                [id:"juros1Dia"],
                [id:"descontoAte"],
                [id:"valorDesconto"],
                [id:"valorIOF"],
                [id:"abatimento"],
                [id:"codigoInscricao"],
                [id:"numeroInscricao"],
                [id:"nomePagador"],
                [id:"filler3"],
                [id:"logradouroPagador"],
                [id:"bairroPagador"],
                [id:"cepPagador"],
                [id:"cidadePagador"],
                [id:"estadoPagador"],
                [id:"sacadorAvalista"],
                [id:"filler4"],
                [id:"dataMora"],
                [id:"prazoDias"],
                [id:"filler5"],
                [id:"sequencial",val:Cnab400Record.nextSequence(),size:6]
        ]

    }
}
