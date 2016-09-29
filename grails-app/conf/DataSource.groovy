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
environments {
    development {

        def devUrl = System.env['AMAZONFROTA_DATABASE_DEVELOPMENT_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://localhost/amazonfrota_development";
        dataSource {
            //logSql = true
            password = "postgres"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }

    }
    homologation {
        def devUrl = System.env['AMAZONFROTA_DATABASE_HOMOLOGATION_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://148.5.7.215/amazonfrota_development";

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
            url = "jdbc:postgresql://localhost/gestaofrota_test"
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
