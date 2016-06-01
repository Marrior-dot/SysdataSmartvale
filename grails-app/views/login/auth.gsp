<head>
<meta name='layout' content='publico' />
<title>Login</title>
%{--<style type='text/css' media='screen'>

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


</style>--}%
</head>

<body>

<div class="row">
	<div class="col-md-4">
		<div class="panel panel-default  col-md-offset-4">
			<div class="panel-heading" id="login-box-home">
				<span class="fa fa-user">
					<g:message code="springSecurity.login.header"
							   style="font-family: 'gloriola_std_mediumregular' !important;"/>
					%{--<img src="http://s11.postimg.org/7kzgji28v/logo_sm_2_mr_1.png" class="img-responsive" alt="Conxole Admin"/>--}%
				</span>
			</div>
			<div class="panel-body">
				<g:if test='${flash.message}'>
					<div class='login_message'
						 style="color: white;">${flash.message}</div>
				</g:if>
				<form action='${request.contextPath}/j_spring_security_check'
					  method='POST' id='loginForm' autocomplete='off'
					  class="form-signin">
					<fieldset>
						<label class="panel-login">
							<div class="login_result"></div>
						</label>
						<input class="form-control" placeholder="Nome" name='j_username'
							   id='username' type="text"
							   style=" font-family: 'gloriola_std_lightregular';">
						<input class="form-control" placeholder="Senha"
							   name='j_password' id='password' type="password"
							   style=" font-family: 'gloriola_std_lightregular';">
						<br>

						<p id="remember_me_holder">
							<input type='checkbox' class='chk'
								   name='${rememberMeParameter}' id='remember_me'
								   <g:if test='${hasCookie}'>checked='checked'</g:if>/>
							<label for='remember_me'
								   style="color: white;text-transform:uppercase; font-family: 'gloriola_std_display_thinRg'; font-size: 0.900em"><g:message
									code="springSecurity.login.remember.me.label"/></label>
						</p>

						<input class="btn btn-lg btn-success btn-block" style="background-color: #016A37" type="submit"
							   id="submit"
							   value="${message(code: "springSecurity.login.button")} »">
					</fieldset>
				</form>
				<g:link style="color: white; font-weight: bold;" controller="login" action="forgotPassword">
					<g:message code="spring.security.ui.forgotPassword.title" default="Esqueci minha senha"/>
				</g:link>
			</div>
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
