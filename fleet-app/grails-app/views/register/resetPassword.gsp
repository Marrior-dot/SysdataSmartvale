<html>

<head>
<title><g:message code='spring.security.ui.resetPassword.title'/></title>
<meta name='layout' content='register'/>
</head>

<body>

<p/>

<s2ui:form width='475' height='250' elementId='resetPasswordFormContainer'
           titleCode='user.resetPassword.header' center='true'>

	<g:form action='resetPassword' name='resetPasswordForm' autocomplete='off'>
	<g:hiddenField name='t' value='${token}'/>
	<div class="sign-in">

	<br/>
	<h4><g:message code='user.resetPassword.description'/></h4>

	<table>
		<s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.user' bean="${command}"
                             labelCodeDefault='Password' value="${command?.password}"/>

		<s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.user' bean="${command}"
                             labelCodeDefault='Password (again)' value="${command?.password2}"/>
	</table>

		<g:actionSubmit controller="register" class="reset" action="resetPassword" value="Redefinir Senha"></g:actionSubmit>


	</div>
	</g:form>

</s2ui:form>

<script>
$(document).ready(function() {
	$('#password').focus();
});
</script>

</body>
</html>
