<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-publico" charset="UTF-8">
    <title>Nova senha</title>
</head>
<body>
    <div class="col-md-4 col-md-offset-4">
        <div class="panel panel-default">
            <div class="panel-heading">
                Nova Senha
            </div>
            <g:form action="saveNewPassword">
                <g:hiddenField name="id" value="${token?.id}"></g:hiddenField>
                <div class="panel-body">
                    <alert:all/>
                    <div class="alert alert-warning" role="alert">
                        Sua senha deve seguir o seguinte padrão:
                        <ul>
                            <li>Ter, no mínimo, 14 caracteres</li>
                            <li>Ter, no mínimo, uma letra Maiúscula (A-Z)</li>
                            <li>Ter, no mínimo, uma letra Minúscula (a-z)</li>
                            <li>Ter, no mínimo, um dos seguintes caracteres especiais: !@#\%^&*()-_+</li>
                            <li>Não iniciar com os caracteres especiais</li>
                        </ul>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label>Nova Senha</label>
                            <g:field type="password" name="newPassword"></g:field>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label>Confirme nova senha</label>
                            <g:field type="password" name="confirmPassword"></g:field>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-success"> Enviar</button>
                    <g:link uri="/" class="btn btn-default"> Voltar Login</g:link>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>