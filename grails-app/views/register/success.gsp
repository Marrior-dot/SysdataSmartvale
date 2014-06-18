<!DOCTYPE html>
<html>
<head>
	<meta name='layout' content='register'/>
	<title><g:message code='spring.security.ui.register.title'/></title>
	<style type="text/css">
		.order-success {
			vertical-align: middle;
		}
		p {
			margin: 1.2em 0;
		}
		.title {
			font-size:2em; 
			font-weight: bold; 
			color: orange;
		}
		.name {
			color: green;
		}
		.order {
			text-align:center;
			font-size: 1.2em;
			background-color: #EAEAEA;
			padding: 10px;
			margin: 2em 200px;
		}
		.number {
			color: green;
			font-weight: bold;
		}
	</style>

<body>
<div id="register-panel" class="ui-corner-all">
	<h1><g:message code="spring.security.ui.register.description"/></h1>
	<div id="success-panel" class="content register" role="main">
		<div id="success-info">
		<table>
		<tr>
		<td>
			
		</td>
		<td class="order-success">
			<p class="title">Bem vindo</p>
			<p><g:message code="spring.security.ui.register.welcome" default="Bem vindo à MF Racing, seu cadastro foi efetuado com sucesso." args="${[command.username]}"/></p>
		</td>
		</tr>
		</table>
		</div>
	</div>
</div>
</body>
