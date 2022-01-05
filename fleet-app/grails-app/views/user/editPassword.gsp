<%@ page import="com.sysdata.gestaofrota.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title>Alteração de Senha</title>
    </head>
    <body>
        <div class="panel panel-default panel-top">

            <div class="panel-heading">
                <h4>Alteração de Senha</h4>
            </div>

            <g:form method="post" action="saveNewPassword">
                <div class="panel-body">

                    <div class="buttons-top">

                        <g:link action="meuUsuario" class="btn btn-default" id="${sec.loggedInUserInfo(field: 'id')}" >
                            <span class="glyphicon glyphicon-triangle-left"></span>&nbsp;Voltar
                        </g:link>

                    </div>


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
                    <div class="panel panel-default panel-top">

                        <div class="panel-body">
                            <g:hiddenField name="id" value="${userInstance?.id}" />
                            <alert:all/>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <label for="currentPassword">Senha Atual</label>
                                    <g:passwordField class="form-control" name="currentPassword" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <label for="newPassword">Nova Senha</label>
                                    <g:passwordField class="form-control" name="newPassword" />
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-3">
                                    <label for="currentPassword">Confirmação Nova Senha</label>
                                    <g:passwordField class="form-control" name="confirmPassword" />
                                </div>
                            </div>

                        </div>


                    </div>

                </div>

                <div class="panel-footer">
                    <button type="submit" class="btn btn-success">
                        <span class="glyphicon glyphicon-save"></span> Alterar
                    </button>
                </div>
            </g:form>
        </div>
    </body>
</html>
