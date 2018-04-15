package com.sysdata.gestaofrota.proc.faturamento.cobranca

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Arquivo {



    static def makeHeader(clos){
        Cnab400Header header=new Cnab400Header()
        clos.delegate=header
        clos()
    }

    static def makeTrailer(clos){
        Cnab400Trailer trailer=new Cnab400Trailer()
        clos.delegate=trailer
        clos()
    }

}
