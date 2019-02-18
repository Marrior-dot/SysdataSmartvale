<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name='layout' content='layout-publico' />
		%{--<link rel="stylesheet" type="text/css" href="/GestaoFrota/css/login.css">--}%
		<title>Login</title>
	</head>

	<body>
	<div class="col-md-4 ">
		<br/>
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">Login</h3>
			</div>
			<br/>
			<div class="text-center">
				<img src="${resource(dir:'images/projetos/' + grailsApplication.config.project?.pasta, file:'logo-small.png')}" alt="logo">
			</div>

			<div class="panel-body">
				<g:if test='${flash.message}'>
					<div class="alert alert-danger" role="alert">${flash.message}</div>
				</g:if>

				<form action='${request.contextPath}/j_spring_security_check' method='POST' id='loginForm' autocomplete='off' class="form-signin">

					<div class="input-group margin-bottom-sm">
						<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						<input class="form-control" placeholder="Nome" name='j_username' id='username' type="text"/>
					</div>
					<div class="input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-eye-close"></i></span>
						<input class="form-control" placeholder="Senha" name='j_password' id='password' type="password"/>
					</div>


					<div class="checkbox">
						<label>
							<input type="checkbox" name='${rememberMeParameter}'
								   id='${rememberMeParameter}' ${hasCookie ? 'checked' : ''}>
							Permanecer conectado
						</label>
					</div>
					<hr/>

					<button type="submit" class="btn btn-lg btn-primary btn-block" id="submit">
						${message(code: "springSecurity.login.button")} »
					</button>
				</form>
			</div>
			%{--TODO: SERÁ IMPLEMENTADO MAIS TARDE--}%
			%{--<div class="panel-footer">--}%
				%{--<g:link controller="register" action="forgotPassword">Esqueci minha senha</g:link>--}%
			%{--</div>--}%
		</div>
	</div>
	</body>
</html>