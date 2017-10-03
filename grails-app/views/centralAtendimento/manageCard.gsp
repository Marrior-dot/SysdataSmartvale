<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.MotivoCancelamento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

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
                        <div class="form-group col-md-3">
                            <label>Programa/RH</label>
                            <p class="form-control-static">${cartaoInstance?.portador?.unidade}</p>
                        </div>

                        <div class="form-group col-md-3">
                            <label>Nº Cartão</label>
                            <p class="form-control-static">${cartaoInstance?.numero}</p>
                        </div>

                        <div class="form-group col-md-3">
                            <label>Status Cartão</label>
                            <p class="form-control-static">${cartaoInstance?.status?.nome}</p>
                        </div>

                        <g:if test="${goTo=='cancelCard' && cartaoInstance?.status==StatusCartao.ATIVO}">
                            <div class="form-group col-md-3">
                                <label for="motivoCancelamento">Motivo Cancelamento</label>
                                <g:select class="form-control" name="motivoCancelamento" from="${MotivoCancelamento.values().findAll{it!=MotivoCancelamento.DEMISSAO}}" optionName="nome"/>
                            </div>
                        </g:if>
                    </div>

                    <g:if test="${cartaoInstance?.portador?.instanceOf(com.sysdata.gestaofrota.PortadorFuncionario)}">
                        <div class="row">
                            <div class="form-group col-md-4">
                                <label>Nome</label>
                                <p class="form-control-static">${cartaoInstance.portador.funcionario?.nome}</p>
                            </div>
                            <div class="form-group col-md-4">
                                <label>CPF</label>
                                <p class="form-control-static">${cartaoInstance.portador.funcionario?.cpf}</p>
                            </div>

                            <div class="form-group col-md-4">
                                <label>Data Nascimento</label>
                                <p class="form-control-static"><g:formatDate format="dd/MM/yyyy" date="${cartaoInstance.portador.funcionario?.dataNascimento}"/></p>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-3">
                                <label>Endereço</label>
                                <p class="form-control-static">${cartaoInstance?.portador?.funcionario?.endereco?.logradouro}</p>
                            </div>
                            <div class="form-group col-md-3">
                                <label>Bairro</label>
                                <p class="form-control-static">${cartaoInstance?.portador?.funcionario?.endereco?.bairro}</p>
                            </div>
                            <div class="form-group col-md-3">
                                <label>Complemento</label>
                                <p class="form-control-static">${cartaoInstance?.portador?.funcionario?.endereco?.complemento}</p>
                            </div>
                            <div class="form-group col-md-3">
                                <label>Nº</label>
                                <p class="form-control-static">${cartaoInstance?.portador?.funcionario?.endereco?.numero}</p>
                            </div>
                        </div>

                        <div class="row">
                            <div class="form-group col-md-3">
                                <label>Tel.Residencial</label>
                                <p class="form-control-static">${cartaoInstance.portador.funcionario?.telefone ?: "NÃO CADASTRADO"}</p>
                            </div>
                            <div class="form-group col-md-3">
                                <label>Tel.Comercial</label>
                                <p class="form-control-static">${cartaoInstance.portador.funcionario?.telefoneComercial ?: "NÃO CADASTRADO"}</p>
                            </div>
                        </div>
                    </g:if>
                    <g:elseif test="${cartaoInstance?.portador?.instanceOf(com.sysdata.gestaofrota.PortadorMaquina)}">
                        <g:if test="${cartaoInstance?.portador?.maquina?.instanceOf(com.sysdata.gestaofrota.Equipamento)}">
                            <div class="row">
                                <div class="form-group col-md-3">
                                    <label>Código</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.codigo}</p>
                                </div>

                                <div class="form-group col-md-3">
                                    <label>Tipo</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.tipo}</p>
                                </div>

                                <div class="form-group col-md-6">
                                    <label>Descrição</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.descricao}</p>
                                </div>
                            </div>
                        </g:if>
                        <g:elseif test="${cartaoInstance?.portador?.maquina?.instanceOf(com.sysdata.gestaofrota.Veiculo)}">
                            <div class="row">
                                <div class="form-group col-md-3">
                                    <label>Placa</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.placa}</p>
                                </div>

                                <div class="form-group col-md-3">
                                    <label>Marca</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.marca}</p>
                                </div>

                                <div class="form-group col-md-3">
                                    <label>Modelo</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.modelo}</p>
                                </div>

                                <div class="form-group col-md-3">
                                    <label>Ano de Fabricação</label>
                                    <p class="form-control-static">${cartaoInstance?.portador.maquina?.anoFabricacao}</p>
                                </div>
                            </div>
                        </g:elseif>
                    </g:elseif>


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