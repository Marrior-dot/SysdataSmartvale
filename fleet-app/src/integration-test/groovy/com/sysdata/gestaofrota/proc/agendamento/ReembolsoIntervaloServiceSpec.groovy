package com.sysdata.gestaofrota.proc.agendamento

import com.sysdata.gestaofrota.PostoCombustivel
import com.sysdata.gestaofrota.ReembolsoIntervalo
import grails.testing.mixin.integration.Integration
import grails.transaction.*
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class ReembolsoIntervaloServiceSpec extends Specification {

    @Autowired ReembolsoIntervaloService reembolsoIntervaloService

    def setup() {
    }

    def cleanup() {
    }

    void "testar proximo data reembolso: calendario irregular"() {

        when:
            PostoCombustivel empresa = new PostoCombustivel()
            [
                    [inicioIntervalo: 6, fimIntervalo: 20, diaEfetivacao: 21, meses: 1],
                    [inicioIntervalo: 21, fimIntervalo: 5, diaEfetivacao: 6, meses: 1],
            ].each {
                empresa.addToReembolsos(new ReembolsoIntervalo(inicioIntervalo: it.inicioIntervalo,
                                                                fimIntervalo: it.fimIntervalo,
                                                                diaEfetivacao: it.diaEfetivacao,
                                                                meses: it.meses))
            }

            empresa.save()

        then:
            empresa.reembolsos.size() == 2


        when:
            def dataRef = new Date()
            dataRef.set([dayOfMonth: 6, month: 11, year: 2020])
            def proximaDataReemb = reembolsoIntervaloService.calcularProximaDataReembolso(empresa, dataRef.clearTime())

        then:
            def dataEsperada = new Date()
            dataEsperada.set([dayOfMonth: 21, month: 11, year: 2020])
            proximaDataReemb == dataEsperada.clearTime()

    }


    void "testar proximo data reembolso: calendario regular"() {

        when:
            PostoCombustivel empresa = new PostoCombustivel()
            [
                [inicioIntervalo: 1, fimIntervalo: 10, diaEfetivacao: 11, meses: 1],
                [inicioIntervalo: 11, fimIntervalo: 20, diaEfetivacao: 21, meses: 1],
                [inicioIntervalo: 21, fimIntervalo: 31, diaEfetivacao: 31, meses: 1]
            ].each {
                empresa.addToReembolsos(new ReembolsoIntervalo(inicioIntervalo: it.inicioIntervalo,
                                                                        fimIntervalo: it.fimIntervalo,
                                                                        diaEfetivacao: it.diaEfetivacao,
                                                                        meses: it.meses))
            }

            empresa.save()

        then:
            empresa.reembolsos.size() == 3


        when:
            def dataRef = new Date()
            dataRef.set([dayOfMonth: 31, month: 11, year: 2020])
            def proximaDataReemb = reembolsoIntervaloService.calcularProximaDataReembolso(empresa, dataRef.clearTime())

        then:
            def dataEsperada = new Date()
            dataEsperada.set([dayOfMonth: 11, month: 0, year: 2021])
            proximaDataReemb == dataEsperada.clearTime()

    }
}
