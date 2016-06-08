<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'veiculo.label', default: 'Veiculo')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>

            <div class="panel-body">
                <div class="buttons">
                    <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                    <g:link class="btn btn-default" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link>
                    <g:link class="btn btn-default" action="create" params="[unidade_id:unidadeInstance?.id]"><g:message code="default.new.label" args="[entityName]" /></g:link>
                </div>

                <br>
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>

                <g:hasErrors bean="${veiculoInstance}">
                    <div class="errors">
                        <span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span>
                        <g:renderErrors bean="${veiculoInstance}" as="list" />
                    </div>
                </g:hasErrors>

                <g:if test="${action==Util.ACTION_VIEW}">
                    <div class="tabbable">
                        <ul class="nav nav-tabs">
                            <li class="active" ><a href="#tab1" data-toggle="tab">${entityName}</a></li>
                            <li><a href="#tab2" data-toggle="tab">Funcionários</a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab1">
                                <g:render template="basico"/>
                            </div>
                            <div class="tab-pane" id="tab2">
                                <g:render template="funcionarios"/>
                            </div>
                        </div>
                    </div>
                </g:if>
                <g:else>
                    <g:render template="basico"/>
                </g:else>
            </div>
        </div>
    </body>
</html>
