<html>

<head>
<title><g:message code='spring.security.ui.forgotPassword.title'/></title>
<meta name='layout' content='register'/>
</head>

<body>

<p/>

<s2ui:form width='350' height='220' elementId='forgotPasswordFormContainer'
           titleCode='user.forgotPassword.header' center='true'>

	<g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>

	<g:if test='${emailSent}'>
	<br/>
	<g:message code='user.forgotPassword.sent'/>
	</g:if>

	<g:else>

	<br/>
	<h4><g:message code='user.forgotPassword.description'/></h4>

	<table>
		<tr>
			<td><label for="username"><g:message code='user.forgotPassword.username'/></label></td>
			<td><g:textField name="username" size="25" /></td>
		</tr>
	</table>

	<g:actionSubmit controller="register" class="reset" action="forgotPassword" value="Redefinir Senha"></g:actionSubmit>

	</g:else>

	</g:form>
</s2ui:form>

<script>
$(document).ready(function() {
	$('#username').focus();
});
</script>

</body>
</html>
