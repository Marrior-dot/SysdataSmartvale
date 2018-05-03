package com.sysdata.gestaofrota.proc.faturamento.cobranca

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Header extends Cnab400Record {

    Cnab400Header(){
        fields=[
                [id:"tipoRegistro",val:"0"],
                [id:"operacao",val:"1"],
                [id:"literalRemessa",val:"REMESSA"],
                [id:"codigoServico",val:"01"],
                [id:"literalServico",val:"COBRANCA"],
                [id:"agencia"],
                [id:"filler1",type:DataType.NUMERIC,size:2],
                [id:"conta"],
                [id:"contaDv"],
                [id:"filler2",type:DataType.ALPHA,size:8],
                [id:"nomeEmpresa"],
                [id:"codigoBanco"],
                [id:"nomeBanco"],
                [id:"dataGeracao",type:DataType.DATE,format:"ddMMyy"],
                [id:"filler3",type:DataType.ALPHA,size:294],
                [id:"sequencial",val:Cnab400Record.nextSequence(),size:6]
        ]

    }



}
