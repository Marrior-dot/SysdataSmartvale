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

        def urlDev=System.env["FROTA_DEV_DB"]
        if(!urlDev) urlDev="jdbc:postgresql://148.5.7.215/amazonfrota_development"
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
			url = urlDev
			password="postgres"
        }
    }
    test {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:postgresql://148.5.7.215/amazonfrota_development"
			password="postgres"
        }
    }
    production {

        def urlProd="jdbc:postgresql://148.5.7.215/amazonfrota_production"
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
			url = urlProd
			password="jmml72"
        }
    }
}
