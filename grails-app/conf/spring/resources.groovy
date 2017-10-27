// Place your Spring DSL code here
beans = {
//    geradorCartao(application.config.project.geradorCartao){
//    }
    localeResolver(org.springframework.web.servlet.i18n.SessionLocaleResolver) {
        java.util.Locale.setDefault(new Locale("pt", "BR"))
    }
}
