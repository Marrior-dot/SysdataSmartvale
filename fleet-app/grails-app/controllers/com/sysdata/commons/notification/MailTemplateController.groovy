package com.sysdata.commons.notification

class MailTemplateController {

    def index() {
        params.max = params.max ? params.max as int : 10
        params.sort = "name"
        [
            mailTemplateList: MailTemplate.list(params),
            mailTemplateCount: MailTemplate.count()
        ]
    }

    def show() {
        respond MailTemplate.get(params.id as long)
    }

    def create() {
        MailTemplate mailTemplate = new MailTemplate(params)
        render view: 'form', model: [mailTemplate: mailTemplate]
    }

    def edit() {
        MailTemplate mailTemplate = MailTemplate.get(params.id.toLong())
        render view: 'form', model: [mailTemplate: mailTemplate]
    }

    def save() {
        MailTemplate mailTemplate
        if (! params.id)
            mailTemplate = new MailTemplate()
        else
            mailTemplate = MailTemplate.get(params.id as long)
        mailTemplate.properties = params
        mailTemplate.save(flush: true)
        flash.success = "Mail Template #${mailTemplate.id} salvo"
        redirect action: 'show', id: mailTemplate.id
    }

    def delete() {
        MailTemplate mailTemplate = MailTemplate.get(params.id as long)
        if (mailTemplate) {
            flash.success = "Mail Template #${mailTemplate.id} removido"
            mailTemplate.delete(flush: true)
        }
        redirect action: 'index'
    }

}
