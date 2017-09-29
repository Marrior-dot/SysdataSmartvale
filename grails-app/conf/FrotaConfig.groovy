environments {
    development {
        def urlDev = System.env["FROTA_DEV_DB"]
        if (!urlDev) urlDev = "jdbc:postgresql://localhost/vrfrota_development"
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = urlDev
            password = "postgres"
        }
    }

    homologation {
        def devUrl = System.env['FROTA_DB_HOM_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://148.5.7.216/amazonfrota_homologation";

        dataSource {
            password = "postgres"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }
    }

    production {
        def devUrl = System.env['FROTA_DB_PRD_URL']
        if (!devUrl) devUrl = "jdbc:postgresql://148.5.7.211/amazonfrota_production";

        dataSource {
            password = "jmml72"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = devUrl
        }
    }
}
