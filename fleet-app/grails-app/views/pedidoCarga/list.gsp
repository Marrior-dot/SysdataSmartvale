<%@ page import="com.sysdata.gestaofrota.StatusPedidoCarga; com.sysdata.gestaofrota.Util" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="${message(code: 'pedidoCarga.label', default: 'Pedido de Carga')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default panel-top">
            <div class="panel-heading">
                <h4><g:message code="default.list.label" args="[entityName]" /></h4>
            </div>
            <div class="panel-body">

                <alert:all/>

                <a class="btn btn-default" href="${createLink(uri: '/')}">
                    <span class="glyphicon glyphicon-home"></span>
                    <g:message code="default.home.label"/>
                </a>

                <g:link class="btn btn-default" action="create">
                    <span class="glyphicon glyphicon-plus"></span>
                    <g:message code="default.new.label" args="${[entityName]}"/>
                </g:link>


                <g:render template="search" model="${[statusPedidoCarga: statusPedidoCarga]}"/>

                <div class="tabbable">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#pedidoInstancia" data-toggle="tab">Pedidos Realizados</a></li>
                        <li><a href="#pedidoProgramado" data-toggle="tab">Pedidos Programados</a></li>
                    </ul>


                    <div class="tab-content">
                        <div class="tab-pane active" id="pedidoInstancia">
                            <g:render template="listInstancia"></g:render>
                        </div>
                        <div class="tab-pane" id="pedidoProgramado">

                        </div>
                    </div>
                </div>


            </div>
        </div>
    <script>

        $(document).ready(function() {

            $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {

                if (e.target.href.includes('#pedidoProgramado')) {
                    loadPedidosProgramados();
                }
            });
        });

        const loadPedidosProgramados = function() {

            $.get("${createLink(action: 'listProgramados')}")

                    .done(function(template) {
                        $("#pedidoProgramado").html(template);
                    })
                    .fail(function(err) {
                        alert(err);
                    })

        }

    </script>

    </body>

</html>
