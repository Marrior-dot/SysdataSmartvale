<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />

        <gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>

        <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionário')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <br><br>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h4><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h4>
            </div>
            <div class="panel-body">
                <div class="buttons">
                    <a class="btn btn-default" href="${createLink(uri: '/')}"><span class="glyphicon glyphicon-home"></span> <g:message code="default.home.label"/></a>
                </div>
                <div class="body">
                    <g:if test="${flash.message}">
                        <div class="message">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${funcionarioInstance}">
                        <div class="errors">
                            <g:renderErrors bean="${funcionarioInstance}" as="list" />
                        </div>
                    </g:hasErrors>
                    <g:form method="post" action="create" controller="${controller}">
                        <div class="dialog">
                            <fieldset>

                                <h4>Seleção RH e Unidade</h4>
                                <div class="row">
                                    <label class="col-md-10"><span>RH</span>
                                        <div  style="width:300px;padding-bottom:20px;">
                                            <gui:autoComplete
                                                    id="rh"
                                                    controller="rh"
                                                    action="autoCompleteJSON"
                                                    name="rh"
                                                    value="${unidadeInstance?.rh?.nomeFantasia}"
                                            />
                                        </div>
                                    </label>
                                </div>

                                <div class="row">
                                    <label class="col-md-10"><span>Unidade</span>
                                        <div style="width:300px;padding-bottom:20px;">
                                            <gui:autoComplete
                                                    id="unidade"
                                                    controller="unidade"
                                                    action="autoCompleteJSON"
                                                    dependsOn="[value:'rh',useId:true,label:'rhId']"
                                                    name="unidade"
                                            />
                                        </div>
                                    </label>
                                </div>
                            </fieldset>
                        </div>
                        <div class="buttons">
                            <g:submitButton name="create" class="btn btn-default" value="Próximo" />
                        </div>
                    </g:form>
                </div>
            </div>
        </div>

    </body>
</html>
