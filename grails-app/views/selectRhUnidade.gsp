<%@ page import="com.sysdata.gestaofrota.Funcionario" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'funcionario.label', default: 'Funcionário')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /> - [${action}]</h1>
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
                	
                	<h2>Seleção RH e Unidade</h2>
                		<div>
							<label><span>RH</span>
								<div style="width:300px;padding-bottom:20px;">
									<gui:autoComplete
									        id="rh"
									        controller="rh"
									        action="autoCompleteJSON"
									        name="rh" 
									/>	
								</div>
							</label>
                		</div>
					
						<div>
							<label><span>Unidade</span>
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
                    <span class="button"><g:submitButton name="create" class="forward" value="Próximo" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
