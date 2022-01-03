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
                <div class="panel-body">
                    <alert:all/>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Nova Senha</label>
                            <g:field type="password" name="newPassword"></g:field>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <label>Confirme nova senha</label>
                            <g:field type="password" name="confirmPassword"></g:field>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <button type="submit" class="btn btn-success"> Enviar</button>
                </div>
            </g:form>
        </div>
    </div>
</body>
</html>