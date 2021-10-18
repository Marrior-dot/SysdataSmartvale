package com.sysdata.gestaofrota

class AlertTagLib {
    static namespace = "alert"
    /**
     * Cria todos os alertas baseado no flash.
     */
    Closure all = {
        out << successIfExists()
        out << infoIfExists()
        out << warningIfExists()
        out << errorsIfExists()
    }

    /**
     * Cria um alerta do tipo INFO
     */
    Closure info = { attrs, body ->
        out << "<div class=\"alert alert-info\" role=\"alert\">"
        out << "<span class=\"glyphicon glyphicon-info-sign\"></span> "
        out << body()
        out << "</div>"
    }

    /**
     * Cria um alerta do tipo DANGER
     */
    Closure errors = { attrs, body ->
        out << "<div class=\"alert alert-danger\" role=\"alert\">"
        out << "<span class=\"glyphicon glyphicon-exclamation-sign\"></span> "
        out << body()
        out << "</div>"
    }


    /**
     * Cria um alerta do tipo SUCCESS
     */
    Closure success = { attrs, body ->
        out << "<div class=\"alert alert-success\" role=\"alert\">"
        out << "<span class=\"glyphicon glyphicon-ok-sign\"></span> "
        out << body()
        out << "</div>"
    }

    /**
     * Cria um alerta do tipo WARNING
     */
    Closure warning = { attrs, body ->
        out << "<div class=\"alert alert-warning\" role=\"alert\">"
        out << "<span class=\"glyphicon glyphicon-warning-sign\"></span> "
        out << body()
        out << "</div>"
    }

    /**
     * Cria um alerta de erro se existir flash.error
     */
    Closure errorsIfExists = { attr, body ->
        if (flash.error) {
            if (flash.error instanceof List) {
                out << body() << "<div class=\"alert alert-danger\" role=\"alert\">"
                flash.error.each {
                    out << body() << "<span class=\"glyphicon glyphicon-exclamation-sign\"></span> "
                    out << body() << it << "<br/>"
                }
                out << body() << "</div>"
            } else
                out << errors(null, flash.error)
        }
    }

    /**
     * Cria um alerta de info se existir flash.info
     */
    Closure infoIfExists = {
        if (flash.info) out << info(null, flash.info)
    }

    /**
     * Cria um alerta de success se existir flash.success
     */
    Closure successIfExists = {
        if (flash.success) out << success(null, flash.success)
    }

    /**
     * Cria um alerta de warning se existir flash.warning
     */
    Closure warningIfExists = {
        if (flash.warning) out << success(null, flash.warning)
    }
}
