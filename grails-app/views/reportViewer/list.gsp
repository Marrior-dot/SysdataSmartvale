<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="bootstrap-layout" />
        <title>Relatório de ${reportInstance?.name}</title>
    </head>
    <body>
		<%
		    if(dataInicio) params.dataInicio = dataInicio
			if(dataFim) params.dataFim = dataFim
		%>
		<br><br>
        <div class="nav">
            <a class="btn btn-default" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
           	<g:link class="btn btn-default" action="listReports">Lista de Relatórios</g:link>
        </div>
        <div class="body">
            <h1>Relatório de ${reportInstance?.name}</h1>
            <g:if test="${flash.message}">
            	<div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.errors}">
	            <div class="errors">
	            	<span style="font-weight:bold;padding-left:10px">ERROS</span> 
	                <ul>
	                	<g:each in="${flash.errors}" var="err">
	                		<li>${err}</li>
	                	</g:each>
	                </ul>
	            </div>
			</g:if>
			
			<g:form action="list">
				<g:hiddenField name="id" value="${reportInstance?.id}"/>
				
				<g:render template="parameters" model="params"/>
				            
	            <div class="buttons">
	            	<g:submitButton class="btn btn-default list" name="list" value="Listar" />
	            	<g:actionSubmit class="btn btn-default xls" action="export" value="Excel"/>
	            </div>
	            
	            <div class="list">
	            	<g:render template="fields" model="params"/>
				</div>	            	
	            <div class="paginateButtons">
	                <g:paginate total="${rowCount}" params="${params}"/>
	            </div>
			</g:form>
			            
        </div>
    </body>
</html>
