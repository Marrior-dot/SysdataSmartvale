

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Show AuditLogEvent</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">AuditLogEvent List</g:link></span>
        </div>
        <div class="body">
            <h1>Visualizando Audit Log</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table class="show">
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
    </body>
</html>
