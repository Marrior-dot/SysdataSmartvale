<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <g:set var="entityName" value="${message(code: 'rh.label', default: 'RH')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${rhInstance}">
            <div class="errors">
            	<span style="font-weight:bold;padding-left:10px">Erro ao salvar ${entityName} </span> 
                <g:renderErrors bean="${rhInstance}" as="list" />
            </div>
            </g:hasErrors>
           	<g:if test="${action==Util.ACTION_VIEW}">

                <div class="tabbable"> <!-- Only required for left/right tabs -->

                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tab1" data-toggle="tab">Programa</a></li>
                        <li><a href="#tab2" data-toggle="tab">RH</a></li>
                        <li><a href="#tab3" data-toggle="tab">Categorias de Funcionários</a></li>
                        <li><a href="#tab4" data-toggle="tab">Estabelecimentos</a></li>
                    </ul>

                    <div class="tab-content">

                        <div class="tab-pane active" id="tab1">
                            <g:render template="basico"/>
                        </div>
                        <div class="tab-pane" id="tab2">
                            <g:render template="/unidade/search" model="[controller:'unidade',rhId:rhInstance?.id]"/>
                        </div>
                        <div class="tab-pane" id="tab3">
                            <g:render template="/categoriaFuncionario/search" model="[controller:'categoriaFuncionario',rhId:rhInstance?.id]"/>
                        </div>
                        <div class="tab-pane" id="tab4">
                            <g:render template="estabelecimentos" bean="${rhInstance}"/>
                        </div>

                    </div>


                </div>
            </g:if>
            <g:else>
                <g:render template="basico"/>
            </g:else>
		</div>
    </body>
</html>


    
