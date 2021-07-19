package com.sysdata.gestaofrota.proc.cobrancaBancaria

import grails.util.Holders
/*
import org.jrimum.bopepo.Boleto
import org.jrimum.domkee.financeiro.banco.febraban.Titulo
*/


/**
 * Created by acception on 03/05/18.
 */
class ItauCobranca extends BancoCobranca {

    ItauCobranca(){
/*
        banco=BancosSuportados.BANCO_ITAU.create()
        codigoCompensacao=BancosSuportados.BANCO_ITAU.codigoDeCompensacao
*/
        nome="BANCO ITAU SA"
    }

    @Override
    String calcularDacNossoNumero(String nossoNumero) {
        def agencia=Holders.grailsApplication.config.project.administradora.contaBancaria.agencia
        def conta=Holders.grailsApplication.config.project.administradora.contaBancaria.numero
        def carteira=Holders.grailsApplication.config.project.administradora.contaBancaria.carteira
        def aux=agencia+conta+carteira+nossoNumero

        int multip=2
        def soma=0
        for(int i=aux.length()-1;i>=0;i--){
            def prod=(aux[i] as int)*multip
            prod=(prod%10==0)?prod:(int)(prod/10)+prod%10
            //println "$i: ${aux[i]} x $multip = $prod"
            soma+=prod
            multip=(multip-1)==0?2:multip-1
        }
        def resto=(soma%10)?:0
        return resto>0?10-resto:0
    }

/*
    @Override
    void adicionarExtensoes(Boleto boleto, Titulo titulo) {

        String nossoNumeroParaExibicao = sprintf("%d/%s-%s",
                titulo.getContaBancaria().getCarteira().getCodigo(),
                titulo.getNossoNumero(),
                titulo.getDigitoDoNossoNumero());

        boleto.addTextosExtras("txtFcNossoNumero", nossoNumeroParaExibicao);
        boleto.addTextosExtras("txtRsNossoNumero", nossoNumeroParaExibicao);
    }
*/

}
