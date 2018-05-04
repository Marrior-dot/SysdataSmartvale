package com.sysdata.gestaofrota.proc.cobrancaBancaria

import grails.util.Holders
import org.jrimum.bopepo.BancosSuportados

/**
 * Created by acception on 03/05/18.
 */
class ItauCobranca extends BancoCobranca {

    ItauCobranca(){
        banco=BancosSuportados.BANCO_ITAU.create()
    }

    @Override
    String calcularDacNossoNumero(String nossoNumero) {
        def agencia=Holders.grailsApplication.config.project.administradora.contaBancaria.agencia
        def conta=Holders.grailsApplication.config.project.administradora.contaBancaria.numero
        def carteira=Holders.grailsApplication.config.project.administradora.contaBancaria.carteira
        def aux=agencia+conta+carteira+nossoNumero

        final int multip=2
        def soma=0
        for(int i=aux.length()-1;i>=0;i--){
            multip=multip==0?2:multip-1
            def prod=(aux[i] as int)*multip
            prod=(prod%10==0)?prod:(int)(prod/10)+prod%10
            soma+=prod
        }
        return 10-(soma%10)
    }

}
