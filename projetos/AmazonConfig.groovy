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
// environment specific settings


dataSource {
    password = "postgres"
    dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
    url = devUrl
}

environments {
    development {

        def urlDev=System.env["FROTA_DEV_DB"]
        if(!urlDev) urlDev="jdbc:postgresql://148.5.7.215/amazonfrota_development"
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = urlDev
            password = "postgres"
        }

    }
    homologation {
        def devUrl = System.env['AMAZONFROTA_DATABASE_HOMOLOGATION_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://148.5.7.216/amazonfrota_homologation";

        dataSource {
            password = "postgres"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }
    }
    test {
        def devUrl = System.env['AMAZONFROTA_DATABASE_DEVELOPMENT_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://localhost/gestaofrota_test";
        dataSource {
            logSql = true
            password = "postgres"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }
    }
/*    test {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://148.5.7.215/amazonfrota_development"
			password="postgres"
        }
    }*/
    production {

        def devUrl = System.env['AMAZONFROTA_DATABASE_PRODUCTION_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://148.5.7.211/amazonfrota_production";

        dataSource {
            password = "jmml72"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }
    }
}
