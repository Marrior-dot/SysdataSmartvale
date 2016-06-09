

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="bootstrap-layout" />
        <title>Show AuditLogEvent</title>
    </head>
    <body>
    <br><br>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4>Visualizando Audit Log</h4>
        </div>
        <div class="panel-body">
            <div class="nav">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>

                    Home</a>
                <g:link class="btn btn-default" action="list">
                    <span class="glyphicon glyphicon-list"></span>

                    AuditLogEvent List</g:link>
            </div>
            <br><br>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>
                <div class="dialog">
                    <table class="table">
                        <tbody>
                        <tr class="prop">
                            <td valign="top" class="name">Id:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'id')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Usuário:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'actor')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Cadastro:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'className')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">ID Cad:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'persistedObjectId')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Evento:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'eventName')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Propriedade:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'propertyName')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Valor Anterior:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'oldValue')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Novo Valor:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'newValue')}</td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">Data Evento:</td>

                            <td valign="top" class="value">${fieldValue(bean:auditLogEventInstance, field:'dateCreated')}</td>

                        </tr>


                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    </body>
</html>
