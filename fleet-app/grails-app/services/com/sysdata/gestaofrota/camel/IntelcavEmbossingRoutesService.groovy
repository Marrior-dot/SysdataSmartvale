package com.sysdata.gestaofrota.camel

import com.sysdata.gestaofrota.Administradora
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional
import org.apache.camel.LoggingLevel
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.processor.idempotent.FileIdempotentRepository

@Transactional
class IntelcavEmbossingRoutesService {

    private static final SEND_FILES_INTELCAV_ID = "SEND_FILES_INTELCAV_ID"

    def camelContext
    GrailsApplication grailsApplication

    def init() {
        initUploadFilesIntoIntelcav()
    }

    private void initUploadFilesIntoIntelcav() {

        camelContext.addRoutes(new RouteBuilder() {

            @Override
            void configure() throws Exception {

                def baseDir = grailsApplication.config.projeto.arquivos.baseDir
                def srcDir = baseDir + grailsApplication.config.projeto.arquivos.paysmart.dir.saida
                def sentDir = baseDir + grailsApplication.config.projeto.arquivos.paysmart.dir.enviado
                def tgtDir = grailsApplication.config.projeto.arquivos.paysmart.dir.enviar

                def host = grailsApplication.config.projeto.sftp.host
                def port = grailsApplication.config.projeto.sftp.port
                def user = grailsApplication.config.projeto.sftp.user
                def pswd = grailsApplication.config.projeto.sftp.pswd

                def initDelay = 60000+(new Random().nextInt(120000))
                def delay = "10m"
                def indentPath = grailsApplication.config.projeto.arquivos.baseDir + "routes"

                def filenamePattern = "${grailsApplication.config.projeto.cartao.embossing.idCliente}_${Administradora.list().first().bin}_FN_\\d{6}_01.txt"

                def fromFile="file://$srcDir?delay=$delay&initialDelay=$initDelay&include=$filenamePattern&move=${sentDir}/\${file:name}.sent"

                def toFtp = "sftp:$host:$port/$tgtDir?username=$user&password=$pswd&passiveMode=true&disconnect=true"

                from(fromFile)
                        .idempotentConsumer(header("CamelFileName"), FileIdempotentRepository.fileIdempotentRepository(new File("$indentPath/envia.embossing.intelcav.connect")))
                        .log(LoggingLevel.DEBUG, "com.sysdata.gestaofrota.camel.IntelcavEmbossingRoutesService", 'Movendo arquivo embossing cartões ${header.CamelFileName} ...')
                        .to(toFtp)
                        .log(LoggingLevel.DEBUG, "com.sysdata.gestaofrota.camel.IntelcavEmbossingRoutesService", 'Marcando arquivo ${header.CamelFileName} como enviado ...')
                        .to("bean:geracaoArquivoEmbossingService?method=marcarEnviado")
                        .routeId(SEND_FILES_INTELCAV_ID)

            }
        })

    }
}
