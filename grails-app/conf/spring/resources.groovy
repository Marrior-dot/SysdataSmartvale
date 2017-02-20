// Place your Spring DSL code here
beans = {

    geradorCartao(application.config.project.geradorCartao){

    }

    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        defaultLocale = new Locale("pt","BR")
        java.util.Locale.setDefault(defaultLocale)
    }
}
