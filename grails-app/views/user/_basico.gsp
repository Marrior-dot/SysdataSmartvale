<%@page import="com.sysdata.gestaofrota.Processadora"%>
<%@page import="com.sysdata.gestaofrota.Administradora"%>
<%@page import="com.sysdata.gestaofrota.Rh"%>
<%@page import="com.sysdata.gestaofrota.PostoCombustivel"%>
<%@ page import="com.sysdata.gestaofrota.Role" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>

<script type="text/javascript" src="${resource(dir:'js',file:'roles.js') }"></script>

<br/>
<div class="panel panel-default">
	<div class="panel-heading">Dados de Usuário</div>
	<div class="panel-body">
		<g:form method="post">
			<g:hiddenField name="id" value="${userInstance?.id}"/>
			<g:hiddenField name="action" value="${action}"/>
			<% def l = [:]
			l = ownerList.collect {
				if (it instanceof PostoCombustivel) {
					[id  : it.id,
					 nome: "Estabelecimento - " + it.nome]
				} else if (it instanceof Rh) {
					[id  : it.id,
					 nome: "RH - " + it.nome]
				} else if (it instanceof Administradora) {
					[id  : it.id,
					 nome: "Administradora - " + it.nome]
				} else if (it instanceof Processadora) {
					[id  : it.id,
					 nome: "Processadora - " + it.nome]
				}
			} %>

			<div class="row">
				<div class="form-group col-md-6">
					<label for="owner">Organização</label>
					<g:select id="owner" name="owner.id" from="${l}" value="${userInstance?.owner?.id}" class="form-control"
							  optionKey="id" optionValue="nome"/>
				</div>
				<g:if test="${action in [Util.ACTION_NEW]}">
					<div class="form-group col-md-6">
						<label for="role">Papel</label>
						<g:select name="role" id="role" from="" value="" optionKey="id" optionValue="authority" class="form-control"/>
					</div>
				</g:if>
			</div>

			<div class="row">
				<g:if test="${action in [Util.ACTION_EDIT]}">
					<div class="form-group col-md-6">
						<label for="role">Papel</label>
						<g:select name="role" from="${Role.withCriteria{eq("authority",role?.authority)}}"
								  value="" optionKey="id" optionValue="authority" class="form-control"/>
					</div>
				</g:if>
				<g:if test="${action in [Util.ACTION_VIEW]}">
					<div class="form-group col-md-6">
						<label for="role">Papel</label>
						<g:select name="role" from="${Role.withCriteria{eq("authority",role?.authority)}}" value="${role?.authority}"
								  optionKey="id" optionValue="authority" class="form-control"/>
					</div>
				</g:if>
			</div>

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

			<div class="buttons">
				<g:if test="${action in [Util.ACTION_NEW]}">
					<g:actionSubmit class="btn btn-default" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</g:if>
				<g:if test="${action in [Util.ACTION_VIEW]}">
					<g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
					<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</g:if>
				<g:if test="${action in [Util.ACTION_EDIT]}">
					<g:actionSubmit class="btn btn-default" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</g:if>
			</div>
		</g:form>

		<sec:ifAnyGranted roles='ROLE_ADMIN'>
			<form action='${request.contextPath}/j_spring_security_switch_user' method='POST'>
				<input class="btn btn-primary" type='submit' value='Logar'/>
				<input type='hidden' name='j_username' value="${userInstance?.username}"/> <br/>
			</form>
		</sec:ifAnyGranted>
	</div>
</div>





