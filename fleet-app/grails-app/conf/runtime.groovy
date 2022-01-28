import grails.util.Environment

projeto = new ConfigSlurper(Environment.current.name).parse(getClass().classLoader.loadClass("frotaApp"))

server.contextPath = projeto.context

dataSource {
    username = projeto.username
    password = projeto.password
    url = projeto.url
    //logSql = true
}

grails.serverURL = projeto.serverUrl

hibernate {
    cache.queries = false
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    //    format_sql = true
}

dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.postgresql.Driver"
    dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''


    properties {
        initialSize = 5
        maxActive = 20
        minIdle = 5
        maxIdle = 10
    }
}


grails {
    mail {
        host = "172.16.100.20"
        username = "no-reply@sysdata.com.br"
    }
}



/*
grails {
    mail {
        host = "smtp.gmail.com"
        port = 465
        username = "sysdatamail@sysdata.com.br"
        password = "X@zN&8sp+XxBmL*w"
        props = ["mail.smtp.auth":"true",
                 "mail.smtp.socketFactory.port":"465",
                 "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
                 "mail.smtp.socketFactory.fallback":"false"]
    }
}
*/


notifications {
    topics = [
        [name: "pedidocarga.solicitacao", subscribers: ["senderMailService"]],
        [name: "embossingCartoes.geracao", subscribers: ["senderMailService"]],
        [name: "loteembossing.criacao", subscribers: ["senderMailService"]],
        [name: "loteembossing.geracaoArquivo", subscribers: ["senderMailService"]],
    ]
}
