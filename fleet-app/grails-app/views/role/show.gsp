<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.show.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">

                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="index"><span class="glyphicon glyphicon-list"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>
                <g:link class="btn btn-default" action="create"><span class="glyphicon glyphicon-plus"></span>&nbsp;<g:message code="default.new.label" args="[entityName]" /></g:link>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-bordered table-list">
                <tbody>
                    <tr>
                        <th>#</th>
                        <td>${this.role.id}</td>
                    </tr>
                    <tr>
                        <th>Nome</th>
                        <td>${this.role.authority}</td>
                    </tr>
                </tbody>
                </table>

                <g:form resource="${this.role}" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="btn btn-default" action="edit" resource="${this.role}"><span class="glyphicon glyphicon-edit"></span>&nbsp;<g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <button class="btn btn-default" type="submit" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');">
                            <span class="glyphicon glyphicon-remove"></span><g:message code="default.button.delete.label" default="Delete" /></button>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </body>
</html>
