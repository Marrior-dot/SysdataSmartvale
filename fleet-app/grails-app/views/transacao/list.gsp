<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusTransacao" %>
<%@ page import="com.sysdata.gestaofrota.TipoTransacao" %>
<%@ page import="com.sysdata.gestaofrota.StatusControleAutorizacao" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>

</head>

<body>
<br><br>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.list.label" args="[entityName]"/></h4>
    </div>

    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}"><span
                    class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
            <g:link class="btn btn-default" action="list">
                <span class="glyphicon glyphicon-list"></span>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_PROC">
                <g:link class="btn btn-default" action="agendarAll">
                    <span class="glyphicon glyphicon-dashboard"></span>
                    Agendar Todas
                </g:link>
            %{--<g:link class="btn btn-default" action="simulador">
                <span class="glyphicon glyphicon-facetime-video"></span>
                Simulação de Transações
            </g:link> --}%
            </sec:ifAnyGranted>
            <br><br>
        </div>

        <div class="body">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.errors}">
                <div class="errors">
                    <span style="font-weight:bold;padding-left:10px">ERROS</span>
                    <ul>
                        <g:each in="${flash.errors}" var="err">
                            <li>${err}</li>
                        </g:each>
                    </ul>
                </div>
            </g:if>

            <g:render template="filtro" model="[action: 'list']"/>

            <g:render template="tabela"/>
        </div>
    </div>
</div>

</body>
</html>
