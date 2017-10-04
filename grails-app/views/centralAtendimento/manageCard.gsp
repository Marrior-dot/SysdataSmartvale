<%@ page import="com.sysdata.gestaofrota.StatusCartao" %>
<%@ page import="com.sysdata.gestaofrota.MotivoCancelamento" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
		<g:set var="title" value="${goTo == 'unlockNewCard' ? 'Desbloqueio de Novo Cartão' : 'Cancelamento de Cartão'}"/>
		<title>${title}</title>
        <script type="application/javascript">
            function submitForm(motivo) {
                var actionForm = $("form#action-form");
                actionForm.prepend('<input type="hidden" name="motivo" id="motivo" value="' + motivo + '"/>');
                actionForm.submit();
            }
        </script>
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

                <g:form action="${goTo}" name="action-form">
                    <g:hiddenField name="id" value="${cartaoInstance?.id}"/>

                    <g:if test="${cartaoInstance?.status == StatusCartao.EMBOSSING}">
                        <button type="submit" class="btn btn-primary">
                            <i class="glyphicon glyphicon-ok"></i> Desbloquear Cartão
                        </button>
                    </g:if>

                    <g:elseif test="${cartaoInstance?.status == StatusCartao.ATIVO}">
                        <div class="btn-group">
                            <button type="submit" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="glyphicon glyphicon-remove"></i> Cancelar Cartão <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu" style="background-color: #c9302c !important;">
                                <g:each in="${MotivoCancelamento.values()}" var="motivo">
                                    <li><a href="#" onclick="submitForm('${motivo.key}')">${motivo}</a></li>
                                </g:each>
                            </ul>
                        </div>
                    </g:elseif>
				</g:form>
			</div>
		</div>
	</div>
	</body>
</html>