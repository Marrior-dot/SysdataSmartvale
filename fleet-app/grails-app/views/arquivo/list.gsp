
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'arquivo.label', default: 'Arquivo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading"><h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">

            <g:if test="${flash.message}">
                <div class="alert alert-info" role="alert">${flash.message}</div>
            </g:if>

            <g:link class="btn btn-default" action="generateEmbossing">
                <span class="glyphicon glyphicon-file"></span>&nbsp;Gerar Arquivo Embossing</g:link>



            <div class="panel-top">
                <g:form>
                    <g:render template="search"/>
                </g:form>
            </div>
        </div>
    </div>
    </body>
</html>
