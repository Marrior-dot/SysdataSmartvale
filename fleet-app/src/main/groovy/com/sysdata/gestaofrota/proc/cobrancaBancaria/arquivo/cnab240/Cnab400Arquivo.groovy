package com.sysdata.gestaofrota.proc.cobrancaBancaria.arquivo.cnab240

/**
 * Created by luiz on 14/04/18.
 */
class Cnab400Arquivo {

    static def makeHeader(clos){
        Cnab400Header header=new Cnab400Header()
        clos.delegate=header
        clos()
        header
    }

    static def makeDetail(clos){
        Cnab400Detalhe detail=new Cnab400Detalhe()
        clos.delegate=detail
        clos()
        detail
    }


    static def makeTrailer(clos){
        Cnab400Trailer trailer=new Cnab400Trailer()
        clos.delegate=trailer
        clos()
        trailer
    }

}
