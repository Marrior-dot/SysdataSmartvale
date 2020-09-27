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

    private static RESTClientHelper instance

    static RESTClientHelper getInstance() {
        if (!instance)
            instance = new RESTClientHelper()

        return instance
    }



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

    ResponseData postJSON(suri, spath, data, headers = null) {
        HTTPBuilder http = new HTTPBuilder(suri)
        if (headers) http.setHeaders(headers)
        ResponseData responseData
        withRetries { retries ->
            http.request(Method.POST, ContentType.JSON) { req ->
                body = data
                response.success = { resp, json ->
                    log.debug "POST OK"
                    retries = 0
                    responseData = new ResponseData(statusCode: resp.statusLine.statusCode, body: json, json: json)
                }
                response.failure = { resp, json ->
                    log.debug "POST ERR -> ${resp.status}"
                    retries = 0
                    responseData = new ResponseData(statusCode: resp.status, body: json, json: json )
                }
            }
            return retries
        }
        responseData
    }



}