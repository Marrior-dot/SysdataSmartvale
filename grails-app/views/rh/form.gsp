<%@ page import="com.sysdata.gestaofrota.Util;com.sysdata.gestaofrota.TipoCobranca" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="${message(code: 'rh.label', default: 'Programas')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>
<body>
<br/>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4><g:message code="default.create.label" args="[entityName]"/> - [${action}]</h4>
    </div>

    <div class="panel-body">
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}">
                <span class="glyphicon glyphicon-home"></span>
                <g:message code="default.home.label"/>
            </a>
            <g:link class="btn btn-default" action="list">
                <span class="glyphicon glyphicon-list"></span>
                <g:message code="default.list.label" args="[entityName]"/>
            </g:link>
        </div>
        <hr/>

        <div class="body">
            <g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger">${flash.error}</div>
            </g:if>

            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable"><!-- Only required for left/right tabs -->
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#programas" data-toggle="tab">Programa</a></li>
                        <li><a href="#rh" data-toggle="tab">RH</a></li>
                        <li><a href="#categorias" data-toggle="tab">Categorias de Funcionários</a></li>
                        <li><a href="#estabelecimentos" data-toggle="tab">Estabelecimentos</a></li>
                        <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO}">
                            <li><a href="#fechamentos" data-toggle="tab">Fechamentos</a></li>
                        </g:if>
                    </ul>

                    <div class="panel">
                        <div class="tab-content panel-body">
                            <div class="tab-pane active" id="programas">
                                <g:render template="basico" model="${[rhInstance: rhInstance, action: action]}"/>
                            </div>

                            <div class="tab-pane" id="rh">
                                <g:render template="/unidade/search"
                                          model="[controller: 'unidade', rhId: rhInstance?.id]"/>
                            </div>

                            <div class="tab-pane" id="categorias">
                                <g:render template="/categoriaFuncionario/tab"
                                          model="[controller: 'categoriaFuncionario', rhInstance: rhInstance]"/>
                            </div>

                            <div class="tab-pane" id="estabelecimentos">
                                <g:render template="newEstabelecimentos" bean="${rhInstance}"/>
                            </div>

                            <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.POS_PAGO}">
                                <div class="tab-pane" id="fechamentos">
                                    <g:render template="/fechamento/tab" model="[rhInstance: rhInstance]"/>
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


    
