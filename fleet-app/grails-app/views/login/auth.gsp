<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<meta name='layout' content='layout-publico' />
		%{--<link rel="stylesheet" type="text/css" href="/GestaoFrota/css/login.css">--}%
		<title>Login</title>
	</head>
	<body>
			<div class="col-md-4 col-md-offset-4">
				<br/>
				<div class="panel panel-primary" style="background: none; border: none; box-shadow: none; -webkit-box-shadow: none;">
					<br/>
					<div class="text-center">
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

							<button type="submit" class="btn btn-danger btn-lg btn-block login border" id="submit">
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