<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name='layout' content='publico' />
		<title>Login</title>
		<link rel="stylesheet" href="${resource(dir:'css',file:'login.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'fonts/fonts.css')}" />
	</head>

	<body>

		<div class="hidden-xs wrapper-home">
			<div class="container">
				<div class="row vertical-offset-100">
					<div class="col-md-1 panel panel-default center">
						<div class="panel-body">
							<g:if test='${flash.message}'>
								<div class="alert alert-danger" role="alert">
									${flash.message}
								</div>
							</g:if>
							<div style="text-align: center">
								<img alt="Cooper Combustível" src="/GestaoFrota/images/vr-beneficios-big.png">
								<br><br>
							</div>

							<form action='${request.contextPath}/j_spring_security_check' method='POST' id='loginForm'
								  autocomplete='off' class="form-signin">

								<input class="form-control" placeholder="Nome" name='j_username' id='username' type="text"/>
								<input class="form-control" placeholder="Senha" name='j_password' id='password' type="password"/>
								%{--TODO: SERÁ IMPLEMENTADO MAIS TARDE--}%
								<div class="input-group-addon">
									<g:link controller="register" action="forgotPassword">
										<g:message code="spring.security.ui.forgotPassword.title" default="Esqueci minha senha"/>
									</g:link>
								</div>


								<div class="checkbox">
									<label>
										<input type="checkbox" name='${rememberMeParameter}' id='${rememberMeParameter}'
											${hasCookie ? 'checked' : ''}>
										<g:message code="springSecurity.login.remember.me.label"/>
									</label>
								</div>

								<button type="submit" class="btn btn-lg btn-success btn-block" id="submit">
									${message(code: "springSecurity.login.button")} »
								</button>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>

