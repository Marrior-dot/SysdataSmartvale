package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

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
                [id:"literalServico",val:"COBRANCA",type:DataType.ALPHA,size:15],
                [id:"agencia",type:DataType.NUMERIC,size:4],
                [id:"filler1",val:"00"],
                [id:"conta",type:DataType.NUMERIC,size:5],
                [id:"contaDv"],
                [id:"filler2",type:DataType.ALPHA,size:8],
                [id:"nomeEmpresa",type:DataType.ALPHA,size:30],
                [id:"codigoBanco"],
                [id:"nomeBanco",type:DataType.ALPHA,size:15],
                [id:"dataGeracao",type:DataType.DATE,format:"ddMMyy"],
                [id:"filler3",type:DataType.ALPHA,size:294],
                [id:"sequencial",val:Cnab400Record.nextSequence(),size:6]
        ]

    }



}
