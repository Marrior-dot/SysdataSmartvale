<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.MotivoCancelamento" %>

<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        
        <g:if test="${goTo=='unlockNewCard'}">
	        <title>Desbloqueio de Novo Cartão</title>
        </g:if>
        <g:elseif test="${goTo=='cancelCard'}">
        	<title>Cancelamento de Cartão</title>
        </g:elseif>
        	
	</head>
	<body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            
            <g:if test="${flash.errors}">
	            <div class="errors">
	                <ul>
	                	<g:each in="${flash.errors}" var="err">
	                		<li>${err}</li>
	                	</g:each>
	                </ul>
	            </div>
			</g:if>            
            
            
	        <g:if test="${goTo=='unlockNewCard'}">
		        <h1>Desbloqueio de Novo Cartão</h1>
	        </g:if>
	        <g:elseif test="${goTo=='cancelCard'}">
	        	<h1>Cancelamento de Cartão</h1>
	        </g:elseif>

            
			<g:form>
				<g:hiddenField name="id" value="${cartaoInstance?.id}"></g:hiddenField>
				<fieldset>
					<label><span>Nome</span>${cartaoInstance?.funcionario?.nome}</label>
					<label><span>CPF</span>${cartaoInstance?.funcionario?.cpf}</label>
					<label><span>Nº Cartão</span>${cartaoInstance?.numero}</label>
					<div class="clear"></div>
					<label><span>Programa/RH</span>${cartaoInstance?.funcionario?.unidade?.rh?.nome}-${cartaoInstance?.funcionario?.unidade?.nome}</label>
					<div class="clear"></div>
					<label><span>Endereço</span>${cartaoInstance?.funcionario?.endereco?.logradouro}</label>
					<label><span>Bairro</span>${cartaoInstance?.funcionario?.endereco?.bairro}</label>
					<label><span>Complemento</span>${cartaoInstance?.funcionario?.endereco?.complemento}</label>
					<label><span>Nº</span>${cartaoInstance?.funcionario?.endereco?.numero}</label>
					<div class="clear"></div>
					<label><span>Tel.Residencial</span>(${cartaoInstance?.funcionario?.telefone?.ddd})${cartaoInstance?.funcionario?.telefoneComercial?.numero}</label>
					<label><span>Tel.Comercial</span>(${cartaoInstance?.funcionario?.telefone?.ddd})${cartaoInstance?.funcionario?.telefoneComercial?.numero}</label>
					<div class="clear"></div>
					<label><span>Data Nascimento</span><g:formatDate format="dd/MM/yyyy" date="${cartaoInstance?.funcionario?.dataNascimento}"/></label>
					<label><span>Status Cartão</span>${cartaoInstance?.status?.nome}</label>
					
					<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
						<div>
							<label><span>Motivo Cancelamento</span>
								<g:select name="motivoCancelamento" from="${MotivoCancelamento.values().findAll{it!=MotivoCancelamento.DEMISSAO}}" optionName="nome"></g:select>
							</label>
						</div>
					</g:if>
					
				</fieldset>
				
				<g:if test="${goTo=='unlockNewCard' && cartaoInstance?.status==StatusCartao.EMBOSSING}">
		            <div class="buttons">
		                <span class="button"><g:actionSubmit action="unlockNewCard" class="unlock" value="Desbloquear Cartão" /></span>
		            </div>
				</g:if>
				
				<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
					<div class="buttons">
		                <span class="button"><g:actionSubmit action="cancelCard" class="unlock" value="Cancelar Cartão" /></span>
		            </div>
				</g:if>
				
			</g:form>
        </div>
	</body>
</html>