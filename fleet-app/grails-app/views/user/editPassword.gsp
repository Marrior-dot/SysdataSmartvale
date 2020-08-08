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

                    <div class="buttons">

                        <g:link action="meuUsuario" class="btn btn-default" id="${sec.loggedInUserInfo(field: 'id')}" >
                            <span class="glyphicon glyphicon-triangle-left"></span>&nbsp;Voltar
                        </g:link>

                    </div>


                    <div class="panel panel-default panel-top">

                        <div class="panel-body">
                            <g:hiddenField name="id" value="${userInstance?.id}" />

                            <g:if test="${flash.message}">
                                <div class="alert alert-info">${flash.message}</div>
                            </g:if>
                            <g:if test="${flash.error}">
                                <div class="alert alert-danger">${flash.error}</div>
                            </g:if>

                            <div class="row">
                                <div class="col-md-3">
                                    <label for="currentPassword">Senha Atual</label>
                                    <g:passwordField class="form-control" name="currentPassword" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <label for="newPassword">Nova Senha</label>
                                    <g:passwordField class="form-control" name="newPassword" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <label for="currentPassword">Confirmação Nova Senha</label>
                                    <g:passwordField class="form-control" name="confirmPassword" />
                                </div>
                            </div>

                        </div>


                    </div>

                </div>

                <div class="panel-footer">

                    <button type="submit" class="btn btn-default">
                        <span class="glyphicon glyphicon-save"></span>
                        <g:message code="default.button.update.label" default="Updatea"></g:message>
                    </button>

                </div>
            </g:form>
        </div>
    </body>
</html>
