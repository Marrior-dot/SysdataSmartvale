package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Header extends Cnab400Registro {

    static {
        defCampos=[
                [id:"tipoRegistro"],
                [id:"operacao"],
                [id:"literalRemessa"],
                [id:"codigoServico"],
                [id:"literalServico",type:DataType.ALPHA,size:15],
                [id:"agencia",type:DataType.NUMERIC,size:4],
                [id:"filler1"],
                [id:"conta",type:DataType.NUMERIC,size:5],
                [id:"contaDv",type:DataType.NUMERIC,size:1],
                [id:"filler2",type:DataType.ALPHA,size:8],
                [id:"nomeEmpresa",type:DataType.ALPHA,size:30],
                [id:"codigoBanco"],
                [id:"nomeBanco",type:DataType.ALPHA,size:15],
                [id:"dataGeracao",type:DataType.DATE,format:"ddMMyy"],
                [id:"filler3",type:DataType.ALPHA,size:294],
                [id:"sequencial",type:DataType.NUMERIC,val:{Cnab400Registro.nextSequence()},size:6]
        ]
    }
}
