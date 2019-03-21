import grails.util.Environment
import org.apache.log4j.DailyRollingFileAppender
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html         : ['text/html', 'application/xhtml+xml'],
                     xml          : ['text/xml', 'application/xml'],
                     text         : 'text/plain',
                     js           : 'text/javascript',
                     rss          : 'application/rss+xml',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     rtf          : 'application/rtf',
                     pdf          : 'application/pdf',
                     excel        : 'application/vnd.ms-excel',
                     all          : '*/*',
                     json         : ['application/json', 'text/json'],
                     form         : 'application/x-www-form-urlencoded',
                     multipartForm: 'multipart/form-data'
]
// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

//Os links dos "caminhos de pao" serao baseados no titulo  
breadcrumbs.selector = "title"

// set per-environment serverURL stem for creating absolute links
environments {
    development {
//        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {

        console name: "stdout", layout: pattern(conversionPattern: "[%d{yyyy-MM-dd HH:mm:ss.SSS}] %p %c{4} - %m%n")

        appender new DailyRollingFileAppender(
                name: "logFile",
                datePattern: "'.'yyyy-MM-dd",
                file: System.properties['catalina.base'] + "/logs/fleet.log",
                layout: pattern(conversionPattern: '[%d{yyyy-MM-dd HH:mm:ss.SSS}] - %m%n')
        )


        rollingFile name: "stacktrace", maxFileSize: 1024 * 10, file: System.properties['catalina.base'] + "/logs/fleet-stacktrace.log"

    }

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    warn 'org.mortbay.log'

    debug 'grails.app.controllers',
            'grails.app.domain',
            'grails.app.services'

    info "grails.app"

    root {
        debug 'logFile', 'stdout'
        info 'logFile', 'stdout'
        error()
        additivity = true
    }

}

hibernateFilter.enableFilters = { user ->
    def result = [:]
    //result['transacaoPorEstabelecimento']=['class':com.sysdata.gestaofrota.Transacao,parameters:['estabelecimento_id':user.owner.id]]
    //result['transacaoPorParticipante']=['class':com.sysdata.gestaofrota.Transacao,parameters:['participante_id':user.owner.id]]
    result['transacaoPorRH'] = ['class': com.sysdata.gestaofrota.Transacao, parameters: ['rh_id': user.owner.id]]
    result['transacaoPorPosto'] = ['class': com.sysdata.gestaofrota.Transacao, parameters: ['posto_id': user.owner.id]]
    result['estabelecimentoPorPosto'] = ['class': com.sysdata.gestaofrota.Estabelecimento, parameters: ['empresa_id': user.owner.id]]
    result

}

grails {
    mail {
        host = "148.5.7.50"
    }
//	host = "smtp.gmail.com"
//	port = 465
//	username = "acception.tests@gmail.com"
//	password = "T35t5123x"
//	props = ["mail.smtp.auth":"true",
//	 "mail.smtp.socketFactory.port":"465",
//	 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
//	 "mail.smtp.socketFactory.fallback":"false"]
//	}
}
grails.plugin.springsecurity.ui.register.postResetUrl = '/'
grails.plugin.springsecurity.ui.forgotPassword.emailBody = 'Prezado (a) <br> Clique na URL abaixo para criar uma nova senha<br>'
grails.plugin.springsecurity.ui.forgotPassword.emailFrom = 'no-reply@sysdata.com.br'
grails.plugin.springsecurity.ui.forgotPassword.emailSubject = 'Recuperação de Senha'

grails.plugins.springsecurity.useSwitchUserFilter = true
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.sysdata.gestaofrota.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.sysdata.gestaofrota.UserRole'
grails.plugins.springsecurity.authority.className = 'com.sysdata.gestaofrota.Role'


grails.plugins.springsecurity.rejectIfNoRule = true
grails.plugin.console.enabled = true
grails.plugins.springsecurity.securityConfigType = "Annotation"

def allRoles = ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB', 'ROLE_RH', 'ROLE_LOG', 'ROLE_HELP']
def masterRoles = ['ROLE_PROC', 'ROLE_ADMIN']

grails.plugins.springsecurity.controllerAnnotations.staticRules = [
        '/report/**'                    : ['ROLE_PROC'],
        '/parameterReport/**'           : ['ROLE_PROC'],
        '/fieldReport/**'               : ['ROLE_PROC'],
        '/auditLogEvent/**'             : ['ROLE_PROC'],
        '/j_spring_security_switch_user': ['ROLE_ADMIN', 'isFullyAuthenticated()'],
        '/reportViewer/**'              : allRoles,
        '/produtoEstabelecimento/**'    : allRoles,
        '/estado/**'                    : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/cidade/**'                    : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/marcaVeiculo/**'              : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/banco/**'                     : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/postoCombustivel/**'          : ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB', 'ROLE_LOG', 'ROLE_HELP'],
        '/endereco/**'                  : ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB', 'ROLE_LOG', 'ROLE_HELP'],
        '/estabelecimento/**'           : ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB', 'ROLE_LOG'],
        '/arquivo/**'                   : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/motivoNegacao/**'             : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/parametroSistema/**'          : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/tipoEquipamento/**'           : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/produto/**'                   : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/role/**'                      : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/transacao/**'                 : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/console/**'                   : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/home/dataGraficoResgate'      : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/home/dataGraficoMesResgate'   : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/rh/**'                        : ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH'],
        '/fechamento/**'                : ['ROLE_PROC', 'ROLE_ADMIN'],
        '/fechamento/index'             : ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH'],
        '/processamento/**'             : ['ROLE_PROC'],
        '/cartao/**'                    : ['ROLE_PROC'],

        '/user/**'                      : allRoles,
        '/user/create'                  : masterRoles,
        '/user/list'                    : masterRoles,
        '/user/meusDados'               : allRoles,

        '/plugins/**'                   : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/js/**'                        : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**'                       : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**'                    : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**'                     : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**'                    : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/register/**'                  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/*'                            : ['IS_AUTHENTICATED_FULLY']
]


auditLog {
    actorClosure = { request, session ->
        if (request.applicationContext.springSecurityService.principal instanceof java.lang.String) {
            return request.applicationContext.springSecurityService.principal
        }
        def username = request.applicationContext.springSecurityService.principal?.username
        if (SpringSecurityUtils.isSwitched()) {
            username = SpringSecurityUtils.switchedUserOriginalUsername + " AS " + username
        }
        return username
    }
}

GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)
project = new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('FrotaConfig'))