package com.sysdata.gestaofrota.http

import com.sysdata.gestaofrota.Util
import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

class ResponseData {
    Integer statusCode
    String body
    Map json

    String toString() {
        "\nStatus Code: $statusCode\nBody:\n$body"
    }
}


@Slf4j
class RESTClientHelper {

    private static final byte RETRIES = 3

    private withRetries = { clo ->
        def retries = RETRIES
        while (retries > 0) {
            Thread.sleep(1000)
            try {
                log.debug "Tentativa: ${RETRIES - retries + 1}"
                retries = clo(retries)
            } catch (Exception e) {
                log.error "Erro ao acessar o endpoint : $e.message"
                log.debug Util.stackTraceToString(e)
                retries--
                if (retries > 0) log.warn("Retrying connect endpoint ($retries) ... ")
            }
        }
    }

    ResponseData getJSON(urlBase, spath, squery = null) {
        def http = new HTTPBuilder(urlBase)
        ResponseData responseData
        withRetries { retries ->
            http.request(Method.GET, ContentType.JSON) { req ->
                response.success = { resp, json ->
                    retries = 0
                    responseData = new ResponseData(statusCode: resp.statusLine.statusCode, json: json)
                }
                response.failure = { resp ->
                    retries = 0
                    responseData = new ResponseData(statusCode: resp.statusLine.statusCode)
                }
            }
            return retries
        }
        return responseData
    }


}