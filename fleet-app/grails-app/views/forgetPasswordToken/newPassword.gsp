<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-publico" charset="UTF-8">
    <title>Nova senha</title>
</head>
<body>
<div class="panel panel-default">
    <div class="panel-heading">
        Nova Senha
    </div>
    <alert:all/>
    <div class="panel-body">
        <alert:all/>
        <div class="row">
            <g:form action="requestNewPassword">
                <div class="col-md-4">
                    <label>Nova Senha</label>
                    <g:field type="password" name="newPassword"></g:field>
                </div>
                <div class="col-md-4">
                    <label>Confirme nova senha</label>
                    <g:field type="password" name="confirmPassword"></g:field>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-default"> Enviar</button>
                </div>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>