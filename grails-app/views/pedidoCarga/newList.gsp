
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="bootstrap-layout" />
    <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <style>
    .pagination {
        display: inline-block;
        padding-left: 0;
        margin: 20px 0;
        border-radius: 4px;
    }

    .pagination>li>a, .pagination>li>span {
        position: relative;
        float: left;
        padding: 6px 12px;
        margin-left: -1px;
        line-height: 1.42857143;
        color: #337ab7;
        text-decoration: none;
        background-color: #fff;
        border: 1px solid #ddd;
    }
    </style>
</head>
<body>
<br>
<br>
<div class="body">

    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4><g:message code="default.list.label" args="[entityName]" /></h4>
        </div>
        <div class="panel-body">
            <g:form>
               %{-- <div class="buttons">
                    <span class="button"><g:actionSubmit class="new"
                                                         action="${unidade_id?'create':'selectRhUnidade'}"
                                                         value="${message(code:'default.new.label', args:[entityName]) }"/></span>
                </div>--}%
                <div class="nav" role="navigation">
                    <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                    <g:link class="btn btn-default" action="create"><span class="glyphicon glyphicon-plus"></span> <g:message code="default.new.label" args="[entityName]" /></g:link>
                </div>
                <g:hiddenField name="unidade_id" value="${unidade_id}"/>
                <br>
                <br>
                <div class="list">
                    <table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
                        <thead>
                            <th>ID</th>
                            <th>RH</th>
                            <th>Unidade</th>
                            <th>Data Pedido</th>
                            <th>Data Carga</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </thead>
                        <tbody>
                            <g:each in="${pedidoCargaInstanceList}" status="i" var="pedidoCarga">
                               <tr>
                                   <td>${pedidoCarga.id}</td>
                                   <td>${pedidoCarga.unidade.rh.nomeFantasia}</td>
                                   <td>${pedidoCarga.unidade.nome}</td>
                                   <td><g:formatDate date="${pedidoCarga.dateCreated}" format="dd/MM/yyyy"></g:formatDate></td>
                                   <td><g:formatDate date="${pedidoCarga.dataCarga}" format="dd/MM/yyyy"></g:formatDate></td>
                                   <td><g:formatNumber number="${pedidoCarga.total}" format="#.##0,00" type="currency" currencySymbol="R\$ "></g:formatNumber></td>
                                   <td>${pedidoCarga.status}</td>
                                   <td>Visualizar Detalhes</td>
                               </tr>
                            </g:each>
                        </tbody>
                    </table>
                    <div class="pagination">
                        <g:paginate  total="${pedidoCargaInstanceTotal}" />
                    </div>
                </div>
            </g:form>
        </div>
    </div>

</div>
</body>
</html>
