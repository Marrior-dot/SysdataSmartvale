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

GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)
frota.projeto = new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass("FrotaConfig"))

// environment specific settings
dataSource {
    dbCreate = frota.projeto.dataSource.dbCreate
    url = frota.projeto.dataSource.url
    password = frota.projeto.dataSource.password
}