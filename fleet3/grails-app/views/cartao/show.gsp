
<%@ page import="com.sysdata.gestaofrota.Cartao" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'cartao.label', default: 'Cartao')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-cartao" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-cartao" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list cartao">
			
				<g:if test="${cartaoInstance?.numero}">
				<li class="fieldcontain">
					<span id="numero-label" class="property-label"><g:message code="cartao.numero.label" default="Numero" /></span>
					
						<span class="property-value" aria-labelledby="numero-label"><g:fieldValue bean="${cartaoInstance}" field="numero"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.motivoCancelamento}">
				<li class="fieldcontain">
					<span id="motivoCancelamento-label" class="property-label"><g:message code="cartao.motivoCancelamento.label" default="Motivo Cancelamento" /></span>
					
						<span class="property-value" aria-labelledby="motivoCancelamento-label"><g:fieldValue bean="${cartaoInstance}" field="motivoCancelamento"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.arquivo}">
				<li class="fieldcontain">
					<span id="arquivo-label" class="property-label"><g:message code="cartao.arquivo.label" default="Arquivo" /></span>
					
						<span class="property-value" aria-labelledby="arquivo-label"><g:link controller="arquivo" action="show" id="${cartaoInstance?.arquivo?.id}">${cartaoInstance?.arquivo?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.cvv}">
				<li class="fieldcontain">
					<span id="cvv-label" class="property-label"><g:message code="cartao.cvv.label" default="Cvv" /></span>
					
						<span class="property-value" aria-labelledby="cvv-label"><g:fieldValue bean="${cartaoInstance}" field="cvv"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.via}">
				<li class="fieldcontain">
					<span id="via-label" class="property-label"><g:message code="cartao.via.label" default="Via" /></span>
					
						<span class="property-value" aria-labelledby="via-label"><g:fieldValue bean="${cartaoInstance}" field="via"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="cartao.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${cartaoInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.portador}">
				<li class="fieldcontain">
					<span id="portador-label" class="property-label"><g:message code="cartao.portador.label" default="Portador" /></span>
					
						<span class="property-value" aria-labelledby="portador-label"><g:link controller="portador" action="show" id="${cartaoInstance?.portador?.id}">${cartaoInstance?.portador?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.senha}">
				<li class="fieldcontain">
					<span id="senha-label" class="property-label"><g:message code="cartao.senha.label" default="Senha" /></span>
					
						<span class="property-value" aria-labelledby="senha-label"><g:fieldValue bean="${cartaoInstance}" field="senha"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="cartao.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${cartaoInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${cartaoInstance?.validade}">
				<li class="fieldcontain">
					<span id="validade-label" class="property-label"><g:message code="cartao.validade.label" default="Validade" /></span>
					
						<span class="property-value" aria-labelledby="validade-label"><g:formatDate date="${cartaoInstance?.validade}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${cartaoInstance?.id}" />
					<g:link class="edit" action="edit" id="${cartaoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
