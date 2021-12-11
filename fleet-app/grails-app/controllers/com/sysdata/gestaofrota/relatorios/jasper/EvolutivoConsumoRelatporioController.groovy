package com.sysdata.gestaofrota.relatorios.jasper

import com.sysdata.gestaofrota.Util
import grails.core.GrailsApplication

class EvolutivoConsumoRelatporioController extends JasperBaseRelatorioController {

    GrailsApplication grailsApplication

    def index() { }

    def list() {
            def pars = [:]
            params.logoBanparaBranca = grailsApplication.config.projeto.relatorios.logoBanparaBranco


            OutputStream outputStream = response.outputStream
            try {
                exportToPdf(grailsApplication, "rel_EvolutivoConsumo", params, outputStream)
            } catch (e) {
                log.error "Erro ao gerar relatório: $e.message"
                e.printStackTrace()

            } finally {
                outputStream.flush()
                outputStream.close()
            }

    }
}