<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">
                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="create"><span class="glyphicon glyphicon-plus"></span>&nbsp;<g:message code="default.new.label" args="[entityName]" /></g:link>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <table class="table table-bordered table-list">
                <thead>
                    <th>Role</th>
                    <th>Organização</th>
                </thead>
                    <tbody>
                        <g:each in="${roleList}" var="role">
                            <tr>
                                <td><g:link action="show" id="${role.id}">${role.authority}</g:link> </td>
                                <td>${role.owner?.nome}</td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>


                <div class="pagination">
                    <g:paginate total="${roleCount ?: 0}" />
                </div>


            </div>

        </div>


    </body>
</html>