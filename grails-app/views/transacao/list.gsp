
<%@ page import="com.sysdata.gestaofrota.Transacao" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'transacao.label', default: 'Transacao')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <sec:ifAnyGranted roles="ROLE_PROC">
	            <span class="menuButton"><g:link class="create" action="agendarAll">Agendar Todas</g:link></span>
	            <span class="menuButton"><g:link class="save" action="simulador">Simulação de Transações</g:link></span>
            </sec:ifAnyGranted>
            
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
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
            <div class="list">
                <table>
                    <thead>
                        <tr>
                            <g:sortableColumn property="id" title="ID" />
                            <g:sortableColumn property="dateCreated" title="Data/Hora" />
                            <th><g:message code="transacao.participante.label" default="Funcionário" /></th>
                            <g:sortableColumn property="tipo" title="${message(code: 'transacao.tipo.label', default: '')}" />
                            <g:sortableColumn property="status" title="${message(code: 'transacao.status.label', default: '')}" />
                            <g:sortableColumn property="statusControle" title="${message(code: 'transacao.statusControle.label', default: '')}" />
                            <g:sortableColumn property="valor" title="${message(code: 'transacao.valor.label', default: '')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${transacaoInstanceList}" status="i" var="transacaoInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="show" id="${transacaoInstance.id}">${fieldValue(bean: transacaoInstance, field: "id")}</g:link></td>
                            <td><g:formatDate date="${transacaoInstance.dateCreated}" format="dd/MM/yyyy HH:mm:ss" /></td>
                            <td>${transacaoInstance.participante?.nome}</td>
                            <td>${transacaoInstance.tipo.nome}</td>
                            <td>${transacaoInstance.status.nome}</td>
                            <td>${transacaoInstance.statusControle?.nome}</td>
                            <td><g:formatNumber number="${transacaoInstance.valor}" format="#0.00" /></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${transacaoInstanceTotal}" />
            </div>
        </div>
    </body>
</html>