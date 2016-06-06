<%@ page import="com.sysdata.gestaofrota.Unidade" %>
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'unidade.label', default: 'Unidade')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>

        <div class="panel panel-default">

            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>

            <div class="panel-body">
                <div class="buttons">
                    <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
                    <g:link class="btn btn-default" controller="rh" action="show" params="[id:rhInstance?.id]"><g:message code="rh.label"/></g:link>
                    <g:link class="btn btn-default" action="generateCartaSenha" params="[id:unidadeInstance?.id]">Gerar Carta Senha</g:link>
                </div>

                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${unidadeInstance}">
                    <div class="errors">
                        <span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span>
                        <g:renderErrors bean="${unidadeInstance}" as="list" />
                    </div>
                </g:hasErrors>


                <fieldset style="border:1px solid;font-size:14px;">
                    <label><span>${message(code: 'rh.label', default: 'RH')}</span>${rhInstance?.nome}</label>
                    <div class="clear"></div>
                </fieldset>

                <g:if test="${action==Util.ACTION_VIEW}">

                    <div class="tabbable">

                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#tab1" data-toggle="tab">RH</a> </li>
                            <li><a href="#tab2" data-toggle="tab">Funcionários</a></li>
                            <li><a href="#tab3" data-toggle="tab">Veículos</a></li>
                            <li><a href="#tab4" data-toggle="tab">Equipamentos</a></li>
                        </ul>

                        <div class="tab-content">
                            <div class="tab-pane active" id="tab1">
                                <g:render template="basico" model="[rhId:rhInstance?.id]"/>
                            </div>
                            <div class="tab-pane" id="tab2">
                                <g:render template="/funcionario/search" model="[controller:'funcionario',unidade_id:unidadeInstance?.id]"/>
                            </div>
                            <div class="tab-pane" id="tab3">
                                <g:render template="/veiculo/search" model="[controller:'veiculo',unidade_id:unidadeInstance?.id]"/>
                            </div>
                            <div class="tab-pane" id="tab4">
                                <g:render template="/equipamento/search" model="[controller:'equipamento',unidade_id:unidadeInstance?.id]"/>
                            </div>
                        </div>
                    </div>
                </g:if>
                <g:else>
                    <g:render template="basico" model="[rhId:rhInstance?.id]"/>
                </g:else>
            </div>
        </div>
    </body>
</html>


    
