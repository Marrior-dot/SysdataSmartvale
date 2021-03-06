<%@ page import="com.sysdata.gestaofrota.Unidade" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="layout-restrito" />
    <g:set var="entityName" value="Unidade" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>
            <div class="panel-body">

                <g:if test="${flash.message}">
                    <div class="alert alert-info">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${unidadeInstance}">
                    <div class="errors">
                        <span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span>
                        <g:renderErrors bean="${unidadeInstance}" as="list" />
                    </div>
                </g:hasErrors>

                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>
                <a class="btn btn-default" href="${createLink(controller:'rh',action:'show',id: rhInstance.id)}">
                <span class="glyphicon glyphicon glyphicon-triangle-left"></span>&nbsp;Empresa
                </a>

                <g:link class="btn btn-default" action="create" params="[rhId: rhInstance.id]" >
                    <span class="glyphicon glyphicon-plus"></span>&nbsp;Nova Unidade
                </g:link>

                <div class="well well-lg panel-top">
                    <h4><strong>Empresa</strong></h4>
                    <h5><g:link controller="rh" action="show" id="${rhInstance?.id}">${rhInstance?.cnpj} - ${rhInstance?.nome}</g:link></h5>
                </div>

                <g:if test="${action==Util.ACTION_VIEW}">
                    <div class="tabbable">

                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#tab1" data-toggle="tab">Unidade</a> </li>
                            <li><a href="#tab2" data-toggle="tab">Funcion??rios</a></li>
                            <li><a href="#tab3" data-toggle="tab">Ve??culos</a></li>
                            <li><a href="#tab4" data-toggle="tab">Equipamentos</a></li>
                        </ul>
                        <div class="panel-body">

                            <div class="tab-content">
                                <div class="tab-pane active" id="tab1">
                                    <g:render template="basico" model="[rhId:rhInstance?.id]"/>
                                </div>
                                <div class="tab-pane" id="tab2">
                                    <div id="unidFuncList"></div>
                                </div>
                                <div class="tab-pane" id="tab3">
                                    <g:render template="/veiculo/search" model="[controller:'veiculo',unidade_id:unidadeInstance?.id]"/>
                                </div>
                                <div class="tab-pane" id="tab4">
                                    <g:render template="/equipamento/search" model="[controller:'equipamento',unidade_id:unidadeInstance?.id]"/>
                                </div>

                            </div>
                        </div>
                    </div>
                </g:if>
                <g:else>
                    <g:render template="basico" model="[rhId:rhInstance?.id]"/>
                </g:else>
            </div>
        </div>

<script>
    $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
        if (e.target.href.includes("#tab2")) {
            $.get("${createLink(action: 'listFuncionarios')}", {unidId: "${unidadeInstance?.id}"}, function(data) {
            })
            .done(function(data){
                $("#unidFuncList").html(data);
            })
            .fail(function(xhr, status, err) {
                console.error(xhr.responseText);
            });
        }
    });
</script>
</body>


</html>


    
