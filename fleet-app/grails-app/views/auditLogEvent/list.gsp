

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="layout-restrito" />
        <title>AuditLogEvent List</title>
    </head>
    <body>
        <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4>Listagem Audit Log</h4>
            </div>
            <div class="panel-body">
                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                <br><br>
                <div class="body">
                    <g:form action="list">
                        <g:if test="${flash.message}">
                            <div class="message">${flash.message}</div>
                        </g:if>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h4>Pesquisa por filtro</h4>
                            </div>
                            <div class="panel-body">
                                <fieldset class="search">
                                    <label><span>Usuário:</span><g:textField class="form-control" name="usuario" value="${params.usuario}"/></label>
%{--
                                    <label><span>Data:</span> <gui:datePicker id="data" name="data" value="${params.data}" formatString="dd/MM/yyyy"/></label>

--}%
                                    <label><span>Data:</span> <input class="form-control" id="data" name="data" value="${params.data}" formatString="dd/MM/yyyy"/></label>
                                </fieldset>
                                    <button type="submit" class="btn btn-default">
                                        Listar
                                        <span class="glyphicon glyphicon-search"></span>
                                    </button>
%{--
                                    <g:submitButton class="btn btn-default" name="list" value="Listar" />
--}%
                            </div>
                        </div>

                        <div class="list">
                            <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
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
                      %{--  <div class="paginateButtons">
                            <g:paginate total="${auditLogEventInstanceTotal}" params="${params}"/>
                        </div>--}%
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
