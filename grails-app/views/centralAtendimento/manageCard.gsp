<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.MotivoCancelamento" %>

<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
		<g:set var="title" value="${goTo == 'unlockNewCard' ? 'Desbloqueio de Novo Cartão' : 'Cancelamento de Cartão'}"/>
		<title>${title}</title>
	</head>
	<body>
		<div class="body">
		<br/>
		<g:if test="${flash.message}">
			<div class="alert alert-info" role="alert">${flash.message}</div>
		</g:if>
		<g:if test="${flash.errors}">
			<div class="alert alert-danger" role="alert">
				<g:each var="err" in="${flash.errors}">
					<strong>${err}</strong>
				</g:each>
			</div>
		</g:if>
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4>${title}</h4>
			</div>
			<div class="panel-body">
				<div class="buttons">
					<a type="button" class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
				</div>
				<br/>

				<g:form>
					<g:hiddenField name="id" value="${cartaoInstance?.id}"></g:hiddenField>

					<div class="row">
						<div class="form-group col-md-4">
							<label>Nome</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.nome}</p>
						</div>
						<div class="form-group col-md-4">
							<label>CPF</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.cpf}</p>
						</div>
						<div class="form-group col-md-4">
							<label>Nº Cartão</label>
							<p class="form-control-static">${cartaoInstance?.numero}</p>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label>Programa/RH</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.unidade?.rh?.nome}-${cartaoInstance?.funcionario?.unidade?.nome}</p>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-3">
							<label>Endereço</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.endereco?.logradouro}</p>
						</div>
						<div class="form-group col-md-3">
							<label>Bairro</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.endereco?.bairro}</p>
						</div>
						<div class="form-group col-md-3">
							<label>Complemento</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.endereco?.complemento}</p>
						</div>
						<div class="form-group col-md-3">
							<label>Nº</label>
							<p class="form-control-static">${cartaoInstance?.funcionario?.endereco?.numero}</p>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-3">
							<label>Tel.Residencial</label>
							<p class="form-control-static">(${cartaoInstance?.funcionario?.telefone?.ddd})${cartaoInstance?.funcionario?.telefoneComercial?.numero}</p>
						</div>
						<div class="form-group col-md-3">
							<label>Tel.Comercial</label>
							<p class="form-control-static">(${cartaoInstance?.funcionario?.telefone?.ddd})${cartaoInstance?.funcionario?.telefoneComercial?.numero}</p>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-3">
							<label>Data Nascimento</label>
							<p class="form-control-static"><g:formatDate format="dd/MM/yyyy" date="${cartaoInstance?.funcionario?.dataNascimento}"/></p>
						</div>
						<div class="form-group col-md-3">
							<label>Status Cartão</label>
							<p class="form-control-static">${cartaoInstance?.status?.nome}</p>
						</div>

						<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
							<div class="form-group col-md-4">
								<label for="motivoCancelamento">Motivo Cancelamento</label>
								<g:select class="form-control" name="motivoCancelamento" from="${MotivoCancelamento.values().findAll{it!=MotivoCancelamento.DEMISSAO}}" optionName="nome"/>
							</div>
						</g:if>
					</div>


					<g:if test="${goTo=='unlockNewCard' && cartaoInstance?.status==StatusCartao.EMBOSSING}">
						<g:actionSubmit action="unlockNewCard" class="btn btn-default" value="Desbloquear Cartão"/>
					</g:if>

					<g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
						<g:actionSubmit action="cancelCard" class="btn btn-default" value="Cancelar Cartão" />
					</g:if>
				</g:form>
			</div>
		</div>
	</div>
	</body>
</html>