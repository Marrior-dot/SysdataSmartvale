
<%@ page import="com.sysdata.gestaofrota.ParametroSistema" %>
<%@ page import="com.sysdata.gestaofrota.EscopoParametro" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'parametroSistema.label', default: 'ParametroSistema')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <sec:ifAnyGranted roles="ROLE_PROC">
	            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
            </sec:ifAnyGranted>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
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
    </body>
</html>
