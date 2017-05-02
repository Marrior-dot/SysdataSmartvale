<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.MotivoCancelamento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

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

		<g:elseif test="${goTo=='transfSaldo'}">
			<title>Transferência de Saldo</title>
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

			<g:elseif test="${goTo=='transfSaldo'}">
				<h1>Transferência de Saldo</h1>
			</g:elseif>

			<g:if test="${goTo=='unlockNewCard' || goTo=='cancelCard'}">
				<g:form>
					<g:hiddenField name="id" value="${cartaoInstance?.id}"></g:hiddenField>
					<g:if test="${goTo=='unlockNewCard'}">
						<g:hiddenField name="goTo" value="unlockNewCard"></g:hiddenField>
					</g:if>
					<g:elseif test="${goTo=='cancelCard'}">
						<g:hiddenField name="goTo" value="cancelCard"></g:hiddenField>
					</g:elseif>

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
						<label><span>Status Portador</span>${participante?.status?.nome}</label>

						<g:if test="${goTo=='cancelCard'}">
							<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO && !cartaoInstance.motivoCancelamento}">
								<div>
									<label><span>Motivo Cancelamento</span>
										<g:select name="motivoCancelamento" from="${MotivoCancelamento.values().findAll{it!=MotivoCancelamento.DEMISSAO}}" optionName="nome"></g:select>
									</label>
								</div>
							</g:if>
							<g:else>
								<label><span>Motivo Cancelamento</span>${cartaoInstance?.motivoCancelamento?.nome}</label>
							</g:else>
						</g:if>


					</fieldset>
					<g:if test="${sucesso==false}">
						<g:if test="${goTo=='unlockNewCard' && cartaoInstance?.status==StatusCartao.EMBOSSING}">
							<div class="buttons">
								<span class="button"><g:actionSubmit action="unlockNewCard" class="unlock" value="Desbloquear Cartão" /></span>
							</div>
						</g:if>
						<g:elseif test="${goTo=='unlockNewCard' && cartaoInstance?.status!=StatusCartao.EMBOSSING}">OBS: O cartão deve estar com status "Embossing" para ser desbloqueado</g:elseif>

						<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
							<div class="buttons">
								<span class="button"><g:actionSubmit action="cancelCard" class="unlock" value="Cancelar Cartão" /></span>
							</div>
						</g:if>
					</g:if>


				</g:form>
			</g:if>
			<g:elseif test="${goTo=='transfSaldo'}">
				<g:form>
					<g:hiddenField name="cartaoInstanceDebitoId" value="${cartaoInstanceDebito?.id}"></g:hiddenField>
					<g:hiddenField name="cartaoInstanceCreditoId" value="${cartaoInstanceCredito?.id}"></g:hiddenField>
					<g:hiddenField name="goTo" value="transfSaldo"></g:hiddenField>
					<fieldset>
						<hr>
						<h2>Cartão P/ Transferir</h2>
						<div class="clear"></div>
						<label><span>Nome</span>${cartaoInstanceDebito?.funcionario?.nome}</label>
						<label><span>CPF</span>${cartaoInstanceDebito?.funcionario?.cpf}</label>
						<label><span>Nº Cartão</span>${cartaoInstanceDebito?.numero}</label>
						<div class="clear"></div>
						<label><span>Programa/RH</span>${cartaoInstanceDebito?.funcionario?.unidade?.rh?.nome}-${cartaoInstanceDebito?.funcionario?.unidade?.nome}</label>
						<div class="clear"></div>
						<label><span>Endereço</span>${cartaoInstanceDebito?.funcionario?.endereco?.logradouro}</label>
						<label><span>Bairro</span>${cartaoInstanceDebito?.funcionario?.endereco?.bairro}</label>
						<label><span>Complemento</span>${cartaoInstanceDebito?.funcionario?.endereco?.complemento}</label>
						<label><span>Nº</span>${cartaoInstanceDebito?.funcionario?.endereco?.numero}</label>
						<div class="clear"></div>
						<label><span>Tel.Residencial</span>(${cartaoInstanceDebito?.funcionario?.telefone?.ddd})${cartaoInstanceDebito?.funcionario?.telefoneComercial?.numero}</label>
						<label><span>Tel.Comercial</span>(${cartaoInstanceDebito?.funcionario?.telefone?.ddd})${cartaoInstanceDebito?.funcionario?.telefoneComercial?.numero}</label>
						<div class="clear"></div>
						<label><span>Data Nascimento</span><g:formatDate format="dd/MM/yyyy" date="${cartaoInstanceDebito?.funcionario?.dataNascimento}"/></label>
						<label><span>Status Cartão</span>${cartaoInstanceDebito?.status?.nome}</label>
						<label><span>Status Portador</span>${participanteDebito?.status?.nome}</label>
						<div class="clear"></div>
						<label><span>Saldo Disponível</span>${Util.formatCurrency(cartaoInstanceDebito?.funcionario?.conta?.saldo)}</label>
					</fieldset>
					<div class="clear"></div>
					<div class="clear"></div>
					<hr>
					<fieldset>
						<h2>Cartão P/ Receber Saldo</h2>
						<div class="clear"></div>
						<label><span>Nome</span>${cartaoInstanceCredito?.funcionario?.nome}</label>
						<label><span>CPF</span>${cartaoInstanceCredito?.funcionario?.cpf}</label>
						<label><span>Nº Cartão</span>${cartaoInstanceCredito?.numero}</label>
						<div class="clear"></div>
						<label><span>Programa/RH</span>${cartaoInstanceCredito?.funcionario?.unidade?.rh?.nome}-${cartaoInstanceCredito?.funcionario?.unidade?.nome}</label>
						<div class="clear"></div>
						<label><span>Endereço</span>${cartaoInstanceCredito?.funcionario?.endereco?.logradouro}</label>
						<label><span>Bairro</span>${cartaoInstanceCredito?.funcionario?.endereco?.bairro}</label>
						<label><span>Complemento</span>${cartaoInstanceCredito?.funcionario?.endereco?.complemento}</label>
						<label><span>Nº</span>${cartaoInstanceCredito?.funcionario?.endereco?.numero}</label>
						<div class="clear"></div>
						<label><span>Tel.Residencial</span>(${cartaoInstanceCredito?.funcionario?.telefone?.ddd})${cartaoInstanceCredito?.funcionario?.telefoneComercial?.numero}</label>
						<label><span>Tel.Comercial</span>(${cartaoInstanceCredito?.funcionario?.telefone?.ddd})${cartaoInstanceCredito?.funcionario?.telefoneComercial?.numero}</label>
						<div class="clear"></div>
						<label><span>Data Nascimento</span><g:formatDate format="dd/MM/yyyy" date="${cartaoInstanceCredito?.funcionario?.dataNascimento}"/></label>
						<label><span>Status Cartão</span>${cartaoInstanceCredito?.status?.nome}</label>
						<label><span>Status Portador</span>${participanteCredito?.status?.nome}</label>
						<div class="clear"></div>
						<label><span>Saldo Disponível</span>${Util.formatCurrency(cartaoInstanceCredito?.funcionario?.conta?.saldo)}</label>
					</fieldset>

					<hr>
					<g:if test="${sucesso==false}">
						<div class="buttons">
							<span class="button"><g:actionSubmit action="transfSaldo" class="unlock" value="Transferir Saldo" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Tem certeza que deseja transferir o saldo?')}');"/></span>
						</div>
					</g:if>
				</g:form>
			</g:elseif>
        </div>
	</body>
</html>