
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'frota.css')}" />

        <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>
        <script type="text/javascript" src="${resource(dir:'js/jquery/jquery.inputmask',file:'jquery.inputmask.js') }" ></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'maskFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>

        <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'table-javascript-style.css')}" />

    </head>
    <body>
    <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">
                <g:form>
                    <div class="nav">
                        <div class="buttons">
                            <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                            <g:actionSubmit class="btn btn-default"
                                            action="${unidade_id?'create':'selectRhUnidade'}"
                                            value="${message(code:'default.new.label', args:[entityName]) }"/>
                        </div>
                        <br><br>
                    </div>

                    <div class="body">
                        <g:if test="${flash.message}">
                            <div class="alert alert-info">${flash.message}</div>
                        </g:if>
                            <g:hiddenField name="unidade_id" value="${unidade_id}"/>
                            <div class="list">
                                <g:render template="search"/>
                            </div>
                    </div>
                </g:form>
            </div>
        </div>
    </body>
</html>
