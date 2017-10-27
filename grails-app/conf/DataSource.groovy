import grails.util.Environment
import org.grails.plugin.hibernate.filter.HibernateFilterDomainConfiguration

dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    username = "postgres"
    configClass = HibernateFilterDomainConfiguration
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

// ======================================== ATENÇÃO!! ========================================
//                              NÃO HÁ MAIS VARIAVEIS DE AMBIENTE DE SISTEMA!
// Para permitir flexibilidade na escolha do banco por PROJETO, um arquivo de configuração de projeto
// deve ser definido na raiz do projeto '/FrotaConfig.groovy'.
// Esse arquivo pode ser do tipo Java Properties ou um script ConfigSlurper e deverá conter as variaveis
// de url e password do banco (dentre outras). Dessa forma, ele será usado dinamicamente pela aplicação.
// Esse arquivo DEVE SER IGNORADO PELO GIT pois cada projeto terá um arquivo com valores diferentes.
// ===========================================================================================

GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)
def config = new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('FrotaConfig'))

dataSource {
    dbCreate = config.dbCreate
    url = config.url
    password = config.password
}