<!DOCTYPE html>

<html>
<head>
    <meta name="layout" content="layout-publico" charset="UTF-8">
    <title>Esqueci minha senha</title>
</head>
<body>
    <div class="col-md-4 col-md-offset-4">
        <div class="panel panel-default">
            <div class="panel-heading">
                Esqueci minha Senha
            </div>
            <alert:all/>
            <g:form action="requestNewPassword">
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-6">
                            <label>Insira seu email</label>
                            <g:textField type="email" name="email"></g:textField>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Confirme seu email</label>
                            <g:textField type="email" name="emailConfirm"></g:textField>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-success"> Confirmar</button>
                    <g:link uri="/" class="btn btn-default"> Voltar Login</g:link>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>