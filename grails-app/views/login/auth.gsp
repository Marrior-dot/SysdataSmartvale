<head>
<meta name='layout' content='main' />
<title>Login</title>
<style type='text/css' media='screen'>

#apresentacao{
	float:left;	
	width:615px;
	
	margin:15px 0px; padding:0px;
	
	color: #336600;
    font-size: 15px;
}

#apresentacao ul{
	padding-top:20px;
}
	

#login {
	width:250px;
	float:left;	
	margin:15px 0px; padding:0px;
	text-align:center;
}

#login .inner {
	width:230px;
	height:394px;
	margin:0px auto;
	text-align:left;
	padding:10px;
	background:url("../images/login_panel.png") top left no-repeat;
	
}
#login .inner .fheader {
	padding:4px;
	margin:3px 0px 3px 0;
	color:#2e3741;
	font-size:14px;
	font-weight:bold;
}

#login .inner .cssform {
	padding-top:25px;
}

#login .inner .cssform p {
	clear: left;
	margin: 0;
	padding: 5px 0 8px 0;
	padding-left: 105px;
	margin-bottom: 10px;
	height: 1%;
}
#login .inner .cssform input[type='text'] {
	width: 200px;
}
#login .inner .cssform label {
	font-weight: bold;
	float: left;
	margin-left: -105px;
	width: 100px;
}
#login .inner .login_message {color:#FFA500; font-weight: bold;padding:4px;}
#login .inner .text_ {width:120px;}
#login .inner .chk {height:12px;}


#apresentacao p{
	color:#000000;
	
}

#apresentacao li{
	list-style-image: none;
	font-weight: bold;
	font-size:18px;
}

#apresentacao li.odd{
	color:#99CC33;
}

a:link, a:visited, a:hover{
	color: #333;
	text-align:left;
	padding:10px;
}


</style>
</head>

<body>

   	<div id="logo">
   		<img src="${resource(dir:'images',file:'logo_amazon.jpg') }" alt="Amazon Card"/>
   	</div>


	<div id="apresentacao">
		<p>O Gestão de Frotas proporciona a você as seguintes funcionalidades:</p>
		<ul>
			<li >Gestão de Programas, RHs, Funcionários e Veículos</li>
			<li class="odd">Credenciamento de Estabelecimentos</li>
			<li >Cargas de saldos nos Cartões</li>
			<li class="odd">Realização de transações</li>
			<li >Gestão financeira</li>
			<li class="odd">Controle de acesso</li>
			<li >Relatórios de Gestão</li>
		</ul>
	</div>

	<div id='login'>
		<div class='inner'>
			<div class='fheader'>Entre no sistema!</div>
			<hr>			
			<g:if test='${flash.message}'>
				<div class='login_message'>${flash.message}</div>
			</g:if>
			
			<form action='${postUrl}' method='POST' id='loginForm' class='cssform'>
				
				<fieldset>
					<p>
						<label><span>Login</span><g:textField name="j_username" value="${request.remoteUser}" /></label>
					</p>
					<p>
						<label><span>Senha</span><g:passwordField name="j_password"  /></label>
					</p>
				</fieldset>
				
<%--				<p>--%>
<%--					<label for='remember_me'>Remember me</label>--%>
<%--					<input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me'--%>
<%--					<g:if test='${hasCookie}'>checked='checked'</g:if> />--%>
<%--				</p>--%>
				<p>
					<input type='submit' value='Entrar' />
				</p>
			</form>
			
			<br>
			
			<div>
					<g:link controller='register' action='forgotPassword'>Esqueceu a senha?</g:link>
			</div>
				
		</div>
		
	</div>
<script type='text/javascript'>
<!--
(function(){
	document.forms['loginForm'].elements['j_username'].focus();
})();
// -->
</script>
</body>
