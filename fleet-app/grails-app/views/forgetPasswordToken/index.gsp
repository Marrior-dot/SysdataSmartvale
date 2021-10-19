<!DOCTYPE html>

<html>
<head>
    <meta name="layout" content="layout-publico" charset="UTF-8">
    <title>Esqueci minha senha</title>
</head>
<body>
    <div class="panel panel-default">
        <div class="panel-heading">
            Esqueci minha Senha
        </div>
        <alert:all/>
        <div class="panel-body">
            <div class="row">
                <g:form action="requestNewPassword">
                    <div class="col-md-4">
                        <label>Insira seu email</label>
                        <g:field type="email" name="email"></g:field>
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-default"> Confirmar</button>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</body>
</html>