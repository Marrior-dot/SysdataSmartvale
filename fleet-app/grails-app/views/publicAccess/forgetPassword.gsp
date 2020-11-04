<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-publico">
    <title>Esqueceu a Senha</title>
</head>
<body>

    <div class="col-md-4 col-md-offset-2">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Esqueceu a Senha</h3>
            </div>
            <div class="panel-body">
                <g:form action="forgotPassword">
                    <g:hiddenField name="id" value="${this.user.id}"/>
                    <div class="row">
                        <div class="col-md-3">
                            <label class="control-label">Senha</label>
                            <g:textField name="password" class="form-control"></g:textField>
                        </div>
                    </div>
                </g:form>
            </div>
        </div>
    </div>
</body>
</html>