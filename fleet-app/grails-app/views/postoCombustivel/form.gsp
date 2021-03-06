<%@ page import="com.sysdata.gestaofrota.PostoCombustivel" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <g:set var="entityName" value="Credenciado" />
        <title>${action} ${entityName}</title>
    </head>
    <body>
    <br/>
        <div class="body">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
                </div>
                <div class="panel-body">

                    <alert:all/>

                    <g:hasErrors bean="${postoCombustivelInstance}">
                        <div class="alert alert-danger" role="alert">
                            <strong>Erro ao salvar Empresa</strong>
                            <g:renderErrors bean="${postoCombustivelInstance}" as="list" />
                        </div>
                    </g:hasErrors>

                    <div class="buttons-top">
                        <a type="button" class="btn btn-default" href="${createLink(controller: 'postoCombustivel', action: 'list')}">
                            <i class="glyphicon glyphicon-th-list"></i>
                            <g:message code="default.list.label" args="[entityName]" />
                        </a>

                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
                            <g:if test="${action == Util.ACTION_VIEW}">
                                <a type="button" class="btn btn-default" href="${createLink(controller: 'postoCombustivel', action: 'create')}">
                                    <i class="glyphicon glyphicon-plus"></i>
                                    <g:message code="default.new.label" args="[entityName]" />
                                </a>
                            </g:if>

                        </sec:ifAnyGranted>
                    </div>

                    <div class="panel-top">
                        <g:if test="${action==Util.ACTION_VIEW}">
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#basico" aria-controls="basico" role="tab" data-toggle="tab">Credenciado</a></li>
                                <li role="presentation"><a href="#calendario" aria-controls="calendario" role="tab" data-toggle="tab">Calend??rio Reembolso</a></li>
                                <li role="presentation"><a href="#estabelecimento" aria-controls="estabelecimento" role="tab" data-toggle="tab">Estabelecimentos</a></li>
%{--
                                <li role="presentation"><a href="#lotes" aria-controls="estabelecimento" role="tab" data-toggle="tab">Lotes de Pagamento</a></li>
--}%
                            </ul>

                            <!-- Tab panes -->
                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane active" id="basico">
                                    <g:render template="basico"/>
                                </div>
                                <div role="tabpanel" class="tab-pane" id="calendario">
                                    <g:render template="listReembolso"/>
                                </div>
                                <div role="tabpanel" class="tab-pane" id="estabelecimento">
                                </div>
%{--
                                <div role="tabpanel" class="tab-pane" id="lotes">
                                    <g:render template="/estabelecimento/search" model="[controller:'estabelecimento',empId:postoCombustivelInstance?.id]" />
                                </div>
--}%
                            </div>
                        </g:if>
                        <g:else>
                            <g:render template="basico"/>
                        </g:else>
                    </div>
                </div>
            </div>
        </div>
    <script>
        var loadEstabelecimentos = function(empId) {
            $.get("${createLink(controller: 'estabelecimento', action: 'listByEmpresa')}", { empId: empId}, function(data) {
               $("#estabelecimento").html(data);
            });
        };

        $(document).ready(function() {

            $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
                if (e.target.href.includes('#estabelecimento'))
                    loadEstabelecimentos("${postoCombustivelInstance?.id}");
            });
        });
    </script>
    </body>
</html>
