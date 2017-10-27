
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'arquivo.label', default: 'Arquivo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <br><br><br>
    <div class="panel panel-default">
        <div class="panel-heading">            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <div class="nav">
                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/></a>
                <g:link class="btn btn-default" action="generateEmbossing">
                    <span class="glyphicon glyphicon-file"></span>
                    Gerar Arquivo Embossing</g:link>
            </div>
            <div class="body">
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <g:form>
                    <div class="list">
                        <g:render template="search"/>
                    </div>
                </g:form>
            </div>
        </div>
    </div>

    </body>
</html>
