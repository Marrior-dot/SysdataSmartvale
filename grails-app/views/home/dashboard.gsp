<%@ page import="com.acception.cadastro.Administradora" %>

<!DOCTYPE html>
<head>
    <meta name="layout" content="bootstrap-menu"/>
    <title>Coop - Dashboard</title>

    <link type="text/css" href="${resource(dir: 'css', file: 'index.css')}"/>
    <sec:ifAnyGranted roles="ROLE_ADMINISTRADORA">
        <g:if test="${Administradora.count() == 0}">
            <asset:javascript src="index"/>
        </g:if>
    </sec:ifAnyGranted>

    <asset:javascript src="chart-config-ativacao.js"/>
    <asset:javascript src="chart-config.js"/>
    <g:if test="${listaProgramas}">
    %{--Modal--}%
        <g:javascript>
            $(document).ready(function () {
                $('#modal-container-441898').modal('show');
            });

        </g:javascript>
    </g:if>

    <style>
    #chartjs-tooltip {
        opacity: 0;
        position: absolute;
        background: rgba(0, 0, 0, .7);
        color: white;
        padding: 3px;
        border-radius: 3px;
        -webkit-transition: all .1s ease;
        transition: all .1s ease;
        pointer-events: none;
        -webkit-transform: translate(-50%, 0);
        transform: translate(-50%, 0);
    }

</style>

</head>

<body>
<br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Transações de Resgate (R$)
                <div class="pull-right">
                    <g:form>
                        <g:select name="mesEscolhido" class="form-down-option-graphics"
                                  from="${['Todos', 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez']}" ></g:select>
                        <g:select name="anoEscolhido" class="form-down-option-graphics" from="${2014..2020}"></g:select>
                    </g:form>
                </div>
            </div>
            <div class="panel-body">
                <div id="line-chart-transacoes">

                </div>
            </div>
            <ul class="chart-legend">
                <li>
                    <span style="background-color:#58CE58"></span>
                    Confirmadas
                </li>
                <sec:ifAnyGranted roles="ROLE_SUPORTE, ROLE_MASTER">
                    <li>
                        <span style="background-color:#EA4335"></span>
                        Não Autorizadas
                    </li>
                    <li>
                        <span style="background-color:#4386F5"></span>
                        Desfeitas
                    </li>
                    <li>
                        <span style="background-color:#FBBC05"></span>
                        Pendentes
                    </li>
                    <li>
                        <span style="background-color:#434343"></span>
                        Canceladas
                    </li>
                </sec:ifAnyGranted>
            </ul>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> Transações de Ativação (R$)
                <div class="pull-right">
                    <g:form>
                        <g:select name="mesEscolhidoAtivacao" class="form-down-option-graphics" style="color: black !important;"
                                  from="${['Todos', 'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez']}"></g:select>
                        <g:select name="anoEscolhidoAtivacao" class="form-down-option-graphics" from="${2014..2020}"></g:select>
                    </g:form>

                </div>

            </div>
    <!-- /.panel-heading -->
            <div class="panel-body">
                <div id="line-chart-transacoes-ativacao">

                </div>
            </div>
            <ul class="chart-legend">
                <li>
                    <span style="background-color:#58CE58"></span>
                    Confirmadas
                </li>
                <sec:ifAnyGranted roles="ROLE_SUPORTE, ROLE_MASTER">
                    <li>
                        <span style="background-color:#EA4335"></span>
                        Não Autorizadas
                    </li>
                    <li>
                        <span style="background-color:#4386F5"></span>
                        Desfeitas
                    </li>
                    <li>
                        <span style="background-color:#FBBC05"></span>
                        Pendentes
                    </li>
                    <li>
                        <span style="background-color:#434343"></span>
                        Canceladas
                    </li>
                </sec:ifAnyGranted>
            </ul>
    <!-- /.panel-body -->
        </div>
<asset:javascript src="morris-data"/>
<asset:javascript src="chart-config"/>
<!-- /#page-wrapper -->

<!-- Modal -->
<div class="modal fade" id="modal-container-441898" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="z-index: 6000">
        <div class="modal-content">
            <g:form controller="home" action="escolherPrograma">
                <div class="modal-header">

                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        ×
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        Escolha um Programa
                    </h4>
                </div>

                <div class="modal-body">
                    <g:select class="form-control" id="programa" name="programa.id" from="${listaProgramas}"
                              optionKey="id" required="" value="" noSelection="['': 'Selecione um programa']"/>
                </div>

                <div class="modal-footer">

                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        Fechar
                    </button>
                    <button type="submit" class="btn btn-primary">
                        Ok
                    </button>
                </div>
            </g:form>
        </div>

    </div>

</div>

<script>
    $(document).ready(function () {
        $.ajax({
            type: 'POST',
            url: '${resource(dir:'home',file:'passwordChangeAjax')}',
            dataType: 'json',
            success: function (data) {
                if (data.mustChange == true) {
                    swal({ title: "Atenção!",
                                text: "É necessário alterar sua senha!",
                                type: "warning" },
                            function () {
                                window.location.href = "${createLink(controller: 'user', action: 'meuUsuario')}?changePassword=true"
                            });
                }
            },
            error: function(data){
                alert('erro');
            }
        });


    });

</script>
</body>
</html>