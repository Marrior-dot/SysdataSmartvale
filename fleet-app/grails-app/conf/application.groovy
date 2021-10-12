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

    plugins {
        twitterbootstrap.fixtaglib = true
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

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern: '/',               access: ['permitAll']],
        [pattern: '/error',          access: ['permitAll']],
        [pattern: '/shutdown',       access: ['permitAll']],
        [pattern: '/assets/**',      access: ['permitAll']],
        [pattern: '/**/js/**',       access: ['permitAll']],
        [pattern: '/**/css/**',      access: ['permitAll']],
        [pattern: '/**/images/**',   access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],

        [pattern: '/login/**',                                     access: ['permitAll']],
        [pattern: '/logout/**',                                    access: ['permitAll']],
        [pattern: '/dashboard/**',                                 access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/rh/**',                                        access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/fechamento/**',                                access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/unidade/**',                                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/funcionario/**',                               access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/veiculo/**',                                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/maquina/**',                                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/equipamento/**',                               access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/maquinaMotorizada/**',                         access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/categoriaFuncionario/**',                      access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/arquivo/**',                                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/cidade/**',                                    access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estado/**',                                    access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],



        [pattern: '/postoCombustivel/index/**',                    access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/list/**',                     access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/show/**',                     access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/listAllJSON/**',              access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/getReembolsoSemanal',         access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/getIntervalosReembolso',      access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/postoCombustivel/loadReembolsoIntervalos',     access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],

        [pattern: '/postoCombustivel/**',                          access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        [pattern: '/estabelecimento/index',                 access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estabelecimento/list',                  access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estabelecimento/show/**',               access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estabelecimento/listAllJSON',           access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estabelecimento/listByEmpresa',         access: ['ROLE_ESTAB', 'ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/estabelecimento/**',                    access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        [pattern: '/centralAtendimento/**',                 access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/cartao/**',                             access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/funcionarioCadastro/**',                access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/pedidoCarga/**',                        access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/transacao/**',                          access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH', 'ROLE_ESTAB']],
        [pattern: '/endereco/**',                           access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        [pattern: '/produtoEstabelecimento/**',             access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB']],

        [pattern: '/reportViewer/**',                       access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB']],


        [pattern: '/produto/**',                            access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/marcaVeiculo/**',                       access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/tipoEquipamento/**',                    access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/portadorCorte/**',                      access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/loteEmbossing/**',                      access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/lotePagamento/**',                      access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_PROC_FINANC']],
        [pattern: '/loteRecebimento/**',                    access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_PROC_FINANC']],
        [pattern: '/corteEstabelecimento/**',               access: ['ROLE_PROC']],


        // Relatórios
        [pattern: '/consumoProdutosRelatorio/**',                    access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/demonstrativoDesempenhoRelatorio/**',            access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/baseEstabelecimentosRelatorio/**',               access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/baseFuncionariosRelatorio/**',                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/baseVeiculosRelatorio/**',                       access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/historicoFrotaRelatorio/**',                     access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/controleMensalCargasRelatorio/**',               access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/baseEquipamentosRelatorio/**',                   access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/demoAbastecimentosEstabelecimentoRelatorio/**',  access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/demoAbastecimentosEmpresaRelatorio/**',          access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/demoAbastecimentosAutorizadosRelatorio/**',      access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/AbastecimentoDiarioClienteRelatorio/**',          access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],
        [pattern: '/ExtratoRepasseEstabelecimentosRelatorio/**',      access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_RH']],


        //[pattern: '/motivoNegacao/**',                      access: ['ROLE_PROC', 'ROLE_ADMIN']],
        [pattern: '/motivoNegacao/list',                    access: ['ROLE_ADMIN', 'ROLE_PROC']],
        [pattern: '/motivoNegacao/show/**',                 access: ['ROLE_ADMIN', 'ROLE_PROC']],
        [pattern: '/motivoNegacao/**',                      access: ['ROLE_PROC']],
        [pattern: '/user/**',                               access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB', 'ROLE_RH', 'ROLE_PROC_FINANC']],
        [pattern: '/role/**',                               access: ['ROLE_PROC']],
        [pattern: '/processamento/**',                      access: ['ROLE_PROC']],


        [pattern: '/console/**',                            access: ['ROLE_PROC']],
        [pattern: "/static/console/**",                     access: ['ROLE_PROC']],
        [pattern: "/mockTransacao/**",                      access: ['ROLE_PROC']],
        [pattern: "/configuracaoPropriedade/**",            access: ['ROLE_PROC']],

        [pattern: "/report/**",                             access: ['ROLE_PROC']],
        [pattern: '/fieldReport/**',                        access: ['ROLE_PROC']],
        [pattern: '/parameterReport/**',                    access: ['ROLE_PROC']],


        // Relatórios
        [pattern: '/projecaoReembolsoRelatorio/**',                  access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB']],
        [pattern: '/reembolsoFaturadoRelatorio/**',                  access: ['ROLE_PROC', 'ROLE_ADMIN', 'ROLE_ESTAB']],

]
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



