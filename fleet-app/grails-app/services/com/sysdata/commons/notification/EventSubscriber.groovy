package com.sysdata.commons.notification

interface EventSubscriber {
    void handleEvent(String event, Map data)
}