package com.sysdata.gestaofrota.proc.faturamento.cobranca

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Trailer extends Cnab400Record {

    Cnab400Trailer(){
        fields=[
                [id:"tipoRegistro",val:"9"],
                [id:"filler",type:DataType.ALPHA,size:393],
                [id:"sequencial",val:Cnab400Record.nextSequence(),size:6,type:DataType.NUMERIC]
        ]

    }

}
