import ch.qos.logback.classic.AsyncAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.util.FileSize
import grails.util.Environment

projeto = new ConfigSlurper(Environment.current.name).parse(getClass().classLoader.loadClass("frotaApp"))

final String LOG_NAME = projeto.projectId
final String PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.SSS}] ${LOG_NAME} %p - %m%n"
List<String> appenderList = ["CONSOLE"]

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = PATTERN
    }
}

//if (Environment.current.name.toLowerCase() in ["homologation", "production"]) {
final String LOG_PATH = (System.properties['catalina.base'] ?: '.') + "/logs"

appender("DAILLY", RollingFileAppender){
    file = "${LOG_PATH}/${LOG_NAME}/${LOG_NAME}.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOG_PATH}/${LOG_NAME}.log.%d{yyyy-MM-dd}"
        maxHistory = 90
        totalSizeCap = FileSize.valueOf("1GB")
    }
    encoder(PatternLayoutEncoder) {
        pattern = PATTERN
    }
}

appender("ASYNC", AsyncAppender) {
    appenderRef("DAILLY")
}
appenderList.add("ASYNC")
//}

logger("fleet3", ALL, appenderList, false)
logger("com.sysdata.gestaofrota", ALL, appenderList, false)
logger("com.sysdata.commons", ALL, appenderList, false)
logger("com.fourLions.processingControl", ALL, appenderList, false)
logger("groovyx.net.http", ALL, appenderList, false)
logger("org.apache.camel", INFO, appenderList, false)






/*

// Async Mail

logger('grails.app.jobs.grails.plugin.asyncmail', TRACE, appenderList, false)
logger('grails.app.services.grails.plugin.asyncmail', TRACE, appenderList, false)
logger('grails.plugin.asyncmail', TRACE, appenderList, false)

// Apache Camel
logger("org.apache.camel", ERROR, appenderList, false)
logger("org.apache.camel", DEBUG, appenderList, false)
logger("org.apache.camel", TRACE, appenderList, false)

// Hibernate
logger 'org.hibernate.type.descriptor.sql.BasicBinder', TRACE, appenderList
logger 'org.hibernate.SQL', TRACE, appenderList

*/



root(ERROR, appenderList)