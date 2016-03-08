

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>AuditLogEvent List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
        </div>

        <div class="body">
            <g:form action="list">

                <h1>Listagem Audit Log</h1>
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <fieldset class="search">
                    <h2>Pesquisa por filtro</h2>
                    <label><span>Usuário:</span><g:textField name="usuario" value="${params.usuario}"/></label>
                    <label><span>Data:</span> <gui:datePicker id="data" name="data" value="${params.data}" formatString="dd/MM/yyyy"/></label>
                </fieldset>
                <div class="buttons">
                    <g:submitButton class="list" name="list" value="Listar" />
                </div>
                <div class="list">
                    <table>
                        <thead>
                        <tr>
                            <g:sortableColumn property="id" title="Id" />
                            <g:sortableColumn property="actor" title="Usuário" />
                            <g:sortableColumn property="className" title="Cadastro" />
                            <g:sortableColumn property="eventName" title="Evento" />
                            <g:sortableColumn property="dateCreated" title="Data/Hora" />
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${auditLogEventInstanceList}" status="i" var="auditLogEventInstance">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                                <td><g:link action="show" id="${auditLogEventInstance.id}">${fieldValue(bean:auditLogEventInstance, field:'id')}</g:link></td>
                                <td>${fieldValue(bean:auditLogEventInstance, field:'actor')}</td>
                                <td>${fieldValue(bean:auditLogEventInstance, field:'className')}</td>
                                <td>${fieldValue(bean:auditLogEventInstance, field:'eventName')}</td>
                                <td><g:formatDate format="dd/MM/yyyy HH:mm:ss" date="${auditLogEventInstance.dateCreated}"/></td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="paginateButtons">
                    <g:paginate total="${auditLogEventInstanceTotal}" params="${params}"/>
                </div>
            </g:form>
        </div>

    </body>
</html>
