<%@ page import="com.sysdata.gestaofrota.Util;com.sysdata.gestaofrota.TipoCobranca" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="layout-restrito"/>
    <g:set var="entityName" value="Cliente"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>

    <asset:stylesheet src="plugins/bootstrap-select.css"/>
</head>
<body>
<div class="panel panel-default panel-top">
    <div class="panel-heading">
        <h4><g:message code="default.create.label" args="[entityName]"/> - [${action}]</h4>
    </div>

    <div class="panel-body">

        <a class="btn btn-default" href="${createLink(uri: '/')}">
            <span class="glyphicon glyphicon-home"></span>
            <g:message code="default.home.label"/>
        </a>
        <g:link class="btn btn-default" action="list">
            <span class="glyphicon glyphicon-list"></span>
            <g:message code="default.list.label" args="[entityName]"/>
        </g:link>

        <div class="panel panel-top">

            <g:if test="${flash.message}">
                <div class="alert alert-info">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="alert alert-danger">${flash.error}</div>
            </g:if>

            <g:eachError bean="${rhInstance}">
                <div class="alert alert-danger">
                    <g:message error="${it}"/>
                </div>
            </g:eachError>


            <g:if test="${action == Util.ACTION_VIEW}">
                <div class="tabbable"><!-- Only required for left/right tabs -->
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#empresas" data-toggle="tab">Empresa</a></li>
                        <li><a href="#centroDeCusto" data-toggle="tab">Unidades</a></li>
                        <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.PRE_PAGO}">
                            <li><a href="#perfis" data-toggle="tab">Perfis de Recarga</a></li>
                        </g:if>
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

                            <g:if test="${rhInstance?.modeloCobranca == TipoCobranca.PRE_PAGO}">
                                <div class="tab-pane" id="perfis">
                                    <g:render template="/categoriaFuncionario/tab"
                                              model="[controller: 'categoriaFuncionario', rhInstance: rhInstance]"/>
                                </div>
                            </g:if>

                            <div class="tab-pane" id="estabelecimentos">
                                <g:render template="estabs" bean="${rhInstance}"/>
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

<asset:javascript src="userMessageModal"></asset:javascript>


<script type="application/javascript">
    $(document).ready(function () {
        alterarModeloCobranca();
    });

    function alterarModeloCobranca() {
        var duration = 500;
        var modeloCobranca = $("select#modeloCobranca").val();
        //var modeloCobranca = "PRE_PAGO"
        var pedidoCargaPanel = $("div.panel#pedido-carga");
        var faturaPanel = $("div#fatura");

        if (modeloCobranca === "POS_PAGO") {
            pedidoCargaPanel.find("input").each(function (index, element) {
                element.value = '0';
            });
            pedidoCargaPanel.hide(duration);
            faturaPanel.show(duration);
        }
        else if (modeloCobranca === "PRE_PAGO") {
            pedidoCargaPanel.show(duration);
            faturaPanel.find("input").each(function (index, element) {
                element.value = '0';
            });
            faturaPanel.hide(duration);
        }
    }
</script>


</body>
</html>


    
