package com.sysdata.commons.notification

import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class EventDispatcherService {

    GrailsApplication grailsApplication

    def publish(String event, Map data) {

        if (grailsApplication.config.notifications) {
            if (grailsApplication.config.notifications.topics) {
                def topic = grailsApplication.config.notifications.topics.find { it.name == event }

                if (topic) {
                    topic.subscribers.each {
                        def subscriberService = grailsApplication.mainContext.getBean(it)
                        if (subscriberService instanceof EventSubscriber) {
                            EventSubscriber subscriber = subscriberService as EventSubscriber
                            subscriber.handleEvent(event, data)
                        } else
                            throw new RuntimeException("Service encontrado (${it}) não é do tipo EventSubscriber!")
                    }
                } else
                    log.warn "Nenhum tópico com o nome '${event}' foi encontrado na configuração!"
            } else
                throw new RuntimeException("Esquema de Tópicos para Notificações não definido!")

        } else
            throw new RuntimeException("O serviço de Notificações não está definido na configuração!")
    }
}
