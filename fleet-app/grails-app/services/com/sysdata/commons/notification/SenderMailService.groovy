package com.sysdata.commons.notification

import grails.gorm.transactions.Transactional
import grails.plugin.asyncmail.AsynchronousMailService

@Transactional
class SenderMailService implements EventSubscriber {

    AsynchronousMailService asyncMailService

    def sendMessage(String keyTemplate, Map data) {
        MailTemplate mailTemplate = MailTemplate.findWhere(key: keyTemplate)
        if (mailTemplate) {
            def mailTo = mailTemplate.makeTo(data)
            log.info "Enviando email (assunto: ${mailTemplate.subject}) para '${mailTo}'..."
            asyncMailService.sendMail {
                to mailTo
                if (mailTemplate.ccTo)
                    cc mailTemplate.ccTo
                from mailTemplate.from
                subject mailTemplate.subject
                html mailTemplate.makeBody(data)
            }
        } else
            throw new RuntimeException("Mail Template '${keyTemplate}' não encontrado!")
    }

    @Override
    void handleEvent(String event, Map data) {
        sendMessage(event, data)
    }
}
