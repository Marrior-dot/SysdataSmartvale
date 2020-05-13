<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
    <br/>
        <div class="body">
            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${userInstance}">
                <div class="alert alert-danger" role="alert">
                    <strong>Erro ao salvar ${entityName} </strong>
                    <g:renderErrors bean="${userInstance}" as="list" />
                </div>
            </g:hasErrors>
            <div class="panel panel-default">
                <div class="panel-heading"><h4><g:message code="default.show.label" args="[entityName]" /></h4></div>
                <div class="panel-body">
                    <div class="buttons">
                        <a class="btn btn-default" href="${createLink(uri: '/')}">
                            <span class="glyphicon glyphicon-home"></span>
                            <g:message code="default.home.label"/></a>
                        <g:link class="btn btn-default" action="list">
                            <span class="glyphicon glyphicon-list"></span>
                            <g:message code="default.list.label" args="[entityName]" />
                        </g:link>

                        <g:if test="${action == Util.ACTION_VIEW}">
                            <g:link class="btn btn-default" action="create">
                                <span class="glyphicon glyphicon-plus"></span>
                                <g:message code="default.new.label" args="[entityName]" />
                            </g:link>
                        </g:if>

                    </div>

                    <g:render template="basico" model="${[action: action]}"/>
                </div>
            </div>
        </div>
    </body>
</html>
