<%@ page import="com.sysdata.gestaofrota.Util;com.sysdata.gestaofrota.TipoCobranca" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="Empresa"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4><g:message code="default.create.label" args="[entityName]"/> - [${action}]</h4>
    </div>

    <div class="panel-body">

        <g:if test="${flash.message}">
            <div class="alert alert-info">${flash.message}</div>
        </g:if>
        <g:if test="${flash.error}">
            <div class="alert alert-danger">${flash.error}</div>
        </g:if>


        <a class="btn btn-default" href="${createLink(uri: '/')}">
            <span class="glyphicon glyphicon-home"></span>
            <g:message code="default.home.label"/>
        </a>
        <g:link class="btn btn-default" action="list">
            <span class="glyphicon glyphicon-list"></span>
            <g:message code="default.list.label" args="[entityName]"/>
        </g:link>


        <div class="panel panel-default panel-top">

            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable"><!-- Only required for left/right tabs -->
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#empresas" data-toggle="tab">Empresa</a></li>
                        <li><a href="#centroDeCusto" data-toggle="tab">Unidades</a></li>
                        <li><a href="#perfis" data-toggle="tab">Perfis de Recarga</a></li>
                        <li><a href="#estabelecimentos" data-toggle="tab">Estabelecimentos</a></li>
                        <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO}">
                            <li><a href="#fechamentos" data-toggle="tab">Fechamentos</a></li>
                        </g:if>
                    </ul>

                    <div class="panel">
                        <div class="tab-content panel-body">
                            <div class="tab-pane active" id="empresas">
                                <g:render template="basico" model="${[rhInstance: rhInstance, action: action]}"/>
                            </div>

                            <div class="tab-pane" id="centroDeCusto">
                                <g:render template="/unidade/search"
                                          model="[controller: 'unidade', rhId: rhInstance?.id]"/>
                            </div>

                            <div class="tab-pane" id="perfis">
                                <g:render template="/categoriaFuncionario/tab"
                                          model="[controller: 'categoriaFuncionario', rhInstance: rhInstance]"/>
                            </div>

                            <div class="tab-pane" id="estabelecimentos">
                                <g:render template="newEstabelecimentos" bean="${rhInstance}"/>
                            </div>

                            <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO}">
                                <div class="tab-pane" id="fechamentos">
                                    <g:render template="/fechamento/tab" model="[rhInstance: rhInstance, usuario:usuario]"/>
                                </div>
                            </g:if>
                        </div>
                    </div>
                </div>
            </g:if>
            <g:else>
                <g:render template="basico" model="${[rhInstance: rhInstance, action: action]}"/>
            </g:else>
        </div>
    </div>
</div>

</body>
</html>


    
