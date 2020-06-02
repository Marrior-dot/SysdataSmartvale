
<%@ page import="com.sysdata.gestaofrota.ParametroSistema" %>
<%@ page import="com.sysdata.gestaofrota.EscopoParametro" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'parametroSistema.label', default: 'ParametroSistema')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="body">

        <br>
        <br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                <g:link action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="[entityName]" />
                </g:link>
                <br><br>
                <div class="list">
                    <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                        <thead>
                        <tr>

                            <g:sortableColumn property="id" title="${message(code: 'parametroSistema.id.label', default: 'Id')}" />

                            <g:sortableColumn property="nome" title="${message(code: 'parametroSistema.nome.label', default: 'Nome')}" />

                            <g:sortableColumn property="tipoDado" title="${message(code: 'parametroSistema.tipoDado.label', default: 'Tipo Dado')}" />

                            <g:sortableColumn property="valor" title="${message(code: 'parametroSistema.valor.label', default: 'Valor')}" />

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${parametroSistemaInstanceList}" status="i" var="parametroSistemaInstance">


                            <g:if test="${parametroSistemaInstance.escopo==EscopoParametro.ADMINISTRADORA}">

                                <sec:ifAnyGranted roles="ROLE_ADMIN">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                        <td><g:link action="show" id="${parametroSistemaInstance.id}">${fieldValue(bean: parametroSistemaInstance, field: "id")}</g:link></td>
                                        <td>${fieldValue(bean: parametroSistemaInstance, field: "nome")}</td>
                                        <td>${parametroSistemaInstance.tipoDado?.nome}</td>
                                        <td>${fieldValue(bean: parametroSistemaInstance, field: "valor")}</td>
                                    </tr>
                                </sec:ifAnyGranted>

                            </g:if>

                            <g:elseif test="${parametroSistemaInstance.escopo==EscopoParametro.PROCESSADORA}">

                                <sec:ifAnyGranted roles="ROLE_PROC">
                                    <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                        <td><g:link action="show" id="${parametroSistemaInstance.id}">${fieldValue(bean: parametroSistemaInstance, field: "id")}</g:link></td>
                                        <td>${fieldValue(bean: parametroSistemaInstance, field: "nome")}</td>
                                        <td>${parametroSistemaInstance.tipoDado?.nome}</td>
                                        <td>${fieldValue(bean: parametroSistemaInstance, field: "valor")}</td>
                                    </tr>
                                </sec:ifAnyGranted>
                            </g:elseif>

                        </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="paginateButtons">
                    <g:paginate total="${parametroSistemaInstanceTotal}" />
                </div>
            </div>
            </div>
        </div>


    </body>
</html>
