grails {
    profile = "web"

    codegen.defaultPackage = "fleet3"

    gorm {
        reactor.events = false
        failOnError = true
    }

    mime {
        disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
        types {
            all = '*/*'
            atom = "application/atom+xml"
            css = "text/css"
            csv = "text/csv"
            form = "application/x-www-form-urlencoded"
            html = ["text/html", "application/xhtml+xml"]
            js = "text/javascript"
            json = ["application/json", "text/json"]
            multipartForm = "multipart/form-data"
            pdf = "application/pdf"
            rss = "application/rss+xml"
            text = "text/plain"
            hal = ["application/hal+json", "application/hal+xml"]
            xml = ["text/xml", "application/xml"]
        }
    }

    urlmapping.cache.maxsize = 1000
    controllers.defaultScope = "singleton"
    databinding.dateFormats = ['dd/MM/yyyy', 'dd/MM/yy', 'yyyy-MM-dd HH:mm:ss.S', "yyyy-MM-dd'T'hh:mm:ss'Z'"]
    converters.encoding = "UTF-8"

    views {
        'default' {
            codec = "html"
        }
        gsp {
            encoding = "UTF-8"
            htmlcodec = "xml"
            codecs {
                expression = "html"
                scriptlets = "html"
                taglib = "none"
                staticparts = "none"
            }
        }
    }
}

endpoints.jmx.'unique-names' = true

info {
    app {
        name = '@info.app.name@'
        version = '@info.app.version@'
        grailsVersion = '@info.app.grailsVersion@'
    }
}


spring {
    main.'banner-mode' = "off"
    groovy.template.'check-template-location' = false
}

endpoints {
    enabled = false
    jmx.enabled = true
}

//def projeto = new ConfigSlurper(Environment.current.name).parse(getClass().classLoader.loadClass("frotaApp"))

// Added by the Audit-Logging plugin:
grails.plugin.auditLog.auditDomainClassName = 'com.sysdata.gestaofrota.AuditLogEvent'

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.sysdata.gestaofrota.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.sysdata.gestaofrota.UserRole'
grails.plugin.springsecurity.authority.className = 'com.sysdata.gestaofrota.Role'

grails.plugin.springsecurity.logout.postOnly = false

grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]


grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/',               access: ['permitAll']],
        [pattern: '/error',          access: ['permitAll']],
        [pattern: '/shutdown',       access: ['permitAll']],
        [pattern: '/assets/**',      access: ['permitAll']],
        [pattern: '/**/js/**',       access: ['permitAll']],
        [pattern: '/**/css/**',      access: ['permitAll']],
        [pattern: '/**/images/**',   access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],

        [pattern: '/login/**',                  access: ['permitAll']],
        [pattern: '/logout/**',                 access: ['permitAll']],
        [pattern: '/dashboard/**',              access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/rh/**',                     access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/unidade/**',                access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/funcionario/**',            access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/veiculo/**',                access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/categoriaFuncionario/**',   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/arquivo/**',                access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        [pattern: '/postoCombustivel/**',   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/cartao/**',             access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/pedidoCarga/**',        access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/transacao/**',          access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/endereco/**',           access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        [pattern: '/produto/**',            access: ['ROLE_PROC']],
        [pattern: '/marcaVeiculo/**',       access: ['ROLE_PROC']],
        [pattern: '/tipoEquipamento/**',    access: ['ROLE_PROC']],
        [pattern: '/motivoNegacao/**',      access: ['ROLE_PROC']],
        [pattern: '/user/**',               access: ['ROLE_PROC']],
        [pattern: '/role/**',               access: ['ROLE_PROC']],
        [pattern: '/processamento/**',               access: ['ROLE_PROC']],



        [pattern: '/console/**',        access: ['ROLE_PROC']],
        [pattern: "/static/console/**", access: ['ROLE_PROC']]



]


