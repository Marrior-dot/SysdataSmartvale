package com.sysdata.commons.notification

import groovy.text.GStringTemplateEngine

class MailTemplate {

    Date dateCreated
    String key
    String name
    String subject
    String to
    String ccTo
    String from
    String body

    static constraints = {
        ccTo nullable: true
    }

    static mapping = {
        id generator: 'sequence', params: ['sequence': 'mailtemplate_seq']
        from column: 'from_address'
        to column: 'to_address', type: 'text'
        body type: 'text'
    }

    private static GStringTemplateEngine templateEngine

    private static GStringTemplateEngine getEngine() {
        if (! templateEngine)
            templateEngine = new GStringTemplateEngine(MailTemplate.class.getClassLoader())
        return templateEngine
    }

    def makeBody(Map binding) {
        return getEngine().createTemplate(this.body).make(binding).toString()
    }

    def makeTo(Map binding) {
        return getEngine().createTemplate(this.to).make(binding).toString()
    }
}
