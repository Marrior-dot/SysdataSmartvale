<html>

<head>
    <title><g:message code='spring.security.ui.forgotPassword.title'/></title>
    <meta name='layout' content='publico'/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'login.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'fonts/fonts.css')}"/>
</head>

<body>

<div class="hidden-xs wrapper-home">
    <div class="container">
        <div class="row vertical-offset-100">
            <div class="col-md-1 panel panel-default center">
                <div class="panel-body">
                    <g:if test='${flash.error}'>
                        <div class="alert alert-danger" role="alert">
                            ${flash.error}
                        </div>
                    </g:if>


                    <g:if test='${emailSent}'>
                        <p class="text-success"><g:message code='user.forgotPassword.sent'/></p>
                    </g:if>
                    <g:else>
                        <g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>

                            <p class="text-info">
                                Digite seu nome de usuário e nós enviaremos um link para redefinir sua senha para o endereço de email cadastrado em sua conta.
                            </p>

                            <input class="form-control" placeholder="Email" name='username' id='username' type="email"/>

                            <br/>
                            <button type="submit" class="btn btn-lg btn-success btn-block" id="submit">
                                Enviar »
                            </button>
                        </g:form>

                    </g:else>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
