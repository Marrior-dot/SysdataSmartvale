<%@page import="com.sysdata.gestaofrota.Estabelecimento; com.sysdata.gestaofrota.Processadora"%>
<%@page import="com.sysdata.gestaofrota.Administradora"%>
<%@page import="com.sysdata.gestaofrota.Rh"%>
<%@page import="com.sysdata.gestaofrota.PostoCombustivel"%>
<%@ page import="com.sysdata.gestaofrota.Role" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>


<br/>
<div class="panel panel-default">
	<div class="panel-heading">Dados de Usuário</div>
	<div class="panel-body">
		<g:form method="post">
			<g:hiddenField name="id" value="${userInstance?.id}"/>
			<g:hiddenField name="action" value="${action}"/>
			<% def l = [:]
			l = ownerList.collect {
				if (it.instanceOf(PostoCombustivel)) {
					[id  : it.id,
					 nome: "Estabelecimento - " + it.nome]
				} else if (it.instanceOf(Rh)) {
					[id  : it.id,
					 nome: "RH - " + it.nome]
				} else if (it.instanceOf(Administradora)) {
					[id  : it.id,
					 nome: "Administradora - " + it.nome]
				} else if (it.instanceOf(Processadora)) {
					[id  : it.id,
					 nome: "Processadora - " + it.nome]
				}
			} %>


			<div class="row">
                <g:if test="${userInstance?.owner?.instanceOf(Rh)}">
                    <div class="form-group col-md-6">
                        <label for="owner">Organização</label>
                        <g:select id="owner" name="owner.id" from="${l}" value="${userInstance?.owner?.id}" class="form-control"
                                  optionKey="id" optionValue="nome" disabled="disabled"/>
                    </div>
                </g:if>
                <g:else>
                    <div class="form-group col-md-6">
                        <label for="owner">Organização</label>
                        <g:select id="owner" name="owner.id" from="${l}" value="${userInstance?.owner?.id}" class="form-control"
                                  optionKey="id" optionValue="nome"/>
                    </div>
                </g:else>

			</div>

		%{--
        <g:if test="${userInstance?.owner?.instanceOf(Rh)}">
            <div class="row">
                <div class="form-group col-md-6">
                    <label for="owner.nome">Nome RH</label>
                    <input type="text" class="form-control" name="owner.nome" disabled value="${userInstance?.owner.nome}"/>
                </div>

                <div class="form-group col-md-6">
                    <label for="owner.codigo">Código RH</label>
                    <input type="text" class="form-control" name="owner.codigo" disabled value="${userInstance?.owner.codigo}"/>
                </div>
            </div>
        </g:if>

        <g:elseif test="${userInstance?.owner?.instanceOf(Estabelecimento)}">
            <div class="row">
                <div class="form-group col-md-6">
                    <label for="owner.nome">Nome Estabelecimento</label>
                    <input type="text" class="form-control" name="owner.nome" disabled value="${userInstance?.owner?.nome}"/>
                </div>

                <div class="form-group col-md-6">
                    <label for="owner.codigo">Código Estabelecimento</label>
                    <input type="text" class="form-control" name="owner.codigo" disabled value="${userInstance?.owner?.codigo}"/>
                </div>
            </div>
        </g:elseif>

        <g:elseif test="${userInstance?.owner?.instanceOf(PostoCombustivel)}">
            <div class="row">
                <div class="form-group col-md-6">
                    <label for="owner.nome">Nome Posto Combustível</label>
                    <input type="text" class="form-control" name="owner.nome" disabled value="${userInstance?.owner?.nome}"/>
                </div>

            </div>
        </g:elseif>
--}%

			<div class="row">
				<div class="col-md-6">
					<bs:formField label="Nome" id="name" name="name" value="${userInstance?.name}" class="form-control"/>
				</div>
				<div class="form-group col-md-6">
					<label for="email">Email</label>
					<input type="email" id="email" name="email" value="${userInstance?.email}" class="form-control"/>
				</div>
			</div>

			<g:if test="${action in [Util.ACTION_NEW]}">
				<div class="row">
					<div class="col-md-4">
						<bs:formField label="Login" id="username" name="username" value="${userInstance?.username}" class="form-control"/>
					</div>
					<div class="form-group col-md-4">
						<label for="password">Senha</label>
						<g:passwordField name="password" class="form-control"/>
					</div>
					<div class="form-group col-md-4">
						<label for="confirmPassword">Confirme a Senha</label>
						<g:passwordField name="confirmPassword" class="form-control"/>
					</div>
				</div>
			</g:if>

			<div class="checkbox">
				<label>
					<g:checkBox name="enabled" value="${userInstance?.enabled}"/>
					<strong>Habilitado</strong>
				</label>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">Papéis</div>
				<div class="panel-body">
					<div class="row">
						<div class="form-group col-md-4">

							<%
								def userAuths = []
							    if (action in [Util.ACTION_VIEW, Util.ACTION_EDIT])
									userAuths = userInstance.authorities as List
							%>

							<g:each in="${Role.list()}" var="r" >
								<g:checkBox name="${r.authority}" value="${userAuths.find { it.authority == r.authority }}"></g:checkBox> ${r.authority} <br/>
							</g:each>


						</div>
					</div>
				</div>
			</div>

			<div class="buttons">
				<g:if test="${action in [Util.ACTION_NEW]}">
					<g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</g:if>
				<g:if test="${action in [Util.ACTION_VIEW]}">
					<sec:ifAnyGranted roles='ROLE_ADMIN, ROLE_MASTER'>
						<g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
						<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</sec:ifAnyGranted>
				</g:if>
				<g:if test="${action in [Util.ACTION_EDIT]}">
					<g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:if test="${userInstance?.owner?.instanceOf(Rh)}">
						<g:actionSubmit class="btn btn-default" action="meusDados" value="${message(code: 'default.button.cancel.label', default: 'Cancelar')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" />
					</g:if>
                    <g:else>
                        <g:actionSubmit class="btn btn-default" action="show" value="${message(code: 'default.button.cancel.label', default: 'Cancelar')}" onclick="return confirm('${message(code: 'default.button.cancel.confirm.message', default: 'Are you sure?')}');" />
                    </g:else>
				</g:if>
			</div>
		</g:form>

%{--
		<sec:ifAnyGranted roles='ROLE_PROC'>
			<form action='${request.contextPath}/j_spring_security_switch_user' method='POST'>
				<input class="btn btn-primary" type='submit' value='Logar'/>
				<input type='hidden' name='j_username' value="${userInstance?.username}"/> <br/>
			</form>
		</sec:ifAnyGranted>
--}%
	</div>
</div>




