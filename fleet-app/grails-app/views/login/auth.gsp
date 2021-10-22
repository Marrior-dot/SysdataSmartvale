<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name='layout' content='layout-publico' />
		%{--<link rel="stylesheet" type="text/css" href="/GestaoFrota/css/login.css">--}%
		<title>Login</title>

		<style>
			.panel-login {
				background: none;
				border: none;
				box-shadow: none;
				-webkit-box-shadow: none;
			}

			#titulo p {
				font-size: 60px;
				color: white;
			}

		</style>

	</head>
	<body>
			<div id="titulo" class="col-md-6">
				<p class="text-center"><strong>${grailsApplication.config.projeto.fraseLogin}</strong></p>
			</div>


			<div class="col-md-4 col-md-offset-2">
				<br/>
				<div class="panel panel-primary ${grailsApplication.config.projeto.loginTransparente ? 'panel-login' : ''}" >

					<div class="panel-heading">
						<h3 class="panel-title">Login</h3>
					</div>

					<div style="display: flex; justify-content: center;">
						<asset:image src="projetos/${grailsApplication.config.projeto.pasta}/logo-small.png"/>
					</div>

					<div class="panel-body" style="background: none; border: none; ">
						<g:if test='${flash.message}'>
							<div class="alert alert-danger" role="alert">${flash.message}</div>
						</g:if>

						<form action='authenticate' method='POST' id='loginForm' autocomplete='off' class="form-signin">

							<div class="input-group margin-bottom-sm" style="margin-bottom: 10px;">
								<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
								<input class="form-control" placeholder="Nome" name='username' id='username' type="text"/>
							</div>
							<div class="input-group">
								<span class="input-group-addon"><i class="glyphicon glyphicon-eye-close"></i></span>
								<input class="form-control" placeholder="Senha" name='password' id='password' type="password"/>
							</div>


							<div class="checkbox">
								<label>
									<input type="checkbox" name='${rememberMeParameter}'
										   id='${rememberMeParameter}' ${hasCookie ? 'checked' : ''}>
									<div style="line-height: 20px;">Permanecer conectado</div>
								</label>
							</div>
							<hr/>

							<button type="submit" class="btn btn-lg btn-block btn-login border" id="submit">
								${message(code: "springSecurity.login.button")} »
							</button>
						</form>
					</div>
					<div class="panel-footer text-center">
					    <g:link controller="forgetPasswordToken">Esqueci minha senha</g:link>
					</div>
				</div>
			</div>


	</body>
</html>