package com.sysdata.gestaofrota.proc.cobrancaBancaria

import org.jrimum.domkee.financeiro.banco.febraban.Banco

/**
 * Created by acception on 03/05/18.
 */
abstract class BancoCobranca {

    Banco banco

    static BancoCobranca factoryMethod(codigo){
        BancoCobranca bancoCobranca
        switch(codigo){
            case 341:
                bancoCobranca=new ItauCobranca()
                break
            default:
                throw new RuntimeException("Banco ($codigo) nao tratado!")
        }
        bancoCobranca
    }

    abstract String calcularDacNossoNumero(String nossoNumero)
}
