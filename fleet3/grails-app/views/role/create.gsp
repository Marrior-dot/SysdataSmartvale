<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">

                <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span>&nbsp;<g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="index"><span class="glyphicon glyphicon-list"></span>&nbsp;<g:message code="default.list.label" args="[entityName]" /></g:link>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.role}">
                    <ul class="errors" role="alert">
                    <g:eachError bean="${this.role}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                    </ul>
                </g:hasErrors>

                <div class="panel panel-default panel-top">
                    <g:form resource="${this.role}" method="POST">
                    <div class="panel-body">
                        <div class="row">
                            <div class="form-group col-sm-3">
                                <label>Nome</label>
                                <g:textField name="authority" class="form-control" value="${this.role.authority}"></g:textField>
                            </div>
                            <div class="form-group col-sm-3">
                                <label>Participante</label>
                                <g:textField name="owner" class="form-control" value="${this.role.owner}"></g:textField>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-save"></span>&nbsp;<g:message code="default.button.create.label" default="Create"></g:message>  </button>
                    </div>
                    </g:form>
                </div>
            </div>
        </div>
    </body>
</html>
