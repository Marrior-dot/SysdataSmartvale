package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

import com.sysdata.gestaofrota.proc.cobrancaBancaria.DataType

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Trailer extends Cnab400Registro {

    static {
        defCampos = [
                [id: "tipoRegistro"],
                [id: "filler", type: DataType.ALPHA, size: 393],
                [id:"sequencial",type:DataType.NUMERIC,val:{Cnab400Registro.nextSequence()},size:6]
        ]

    }

}
