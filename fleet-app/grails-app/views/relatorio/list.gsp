
<%@ page import="com.sysdata.gestaofrota.Util" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        
	        <meta name="layout" content="main" />
        
        <g:set var="entityName" value="${message(code: 'proposta.label', default: 'Proposta')}" />
        <title>Relatório de Transações</title>
        
		<export:resource />
    </head>
    <body>
        <%
		def par=[:]
		if (searchDataInicio) par['dataInicio']=dataInicio
		if (searchDataFim) par['dataFim']=dataFim
		
		def dfmt=new java.text.SimpleDateFormat('dd/MM/yy HH:mm')
		 %>
		 
		<div class="nav">
				<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
        </div>
		 
        <div class="body">
            <h1>Relatório de Transações</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="search">
            <g:form >
            	<fieldset>
            		<label><span>Data Início</span><gui:datePicker id="dataInicio" name="dataInicio" value="${dataInicio}" formatString="dd/MM/yyyy"/></label>
            		<label><span>Data Fim</span><gui:datePicker id="dataFim" name="dataFim" value="${dataFim}" formatString="dd/MM/yyyy"/></label>
            	</fieldset>
            	<span style="margin-left:16px; width: 100%; text-align: center"><g:actionSubmit class="search" action="list" value="Filtrar" /></span>

            </g:form>
            </div>
            <div>
            <table>
            	<tr>
            		<td style="vertical-align: middle">Total Registros: ${transacaoInstanceTotal}</td>
            		<td><export:formats formats="['excel', 'pdf']" params="${pars}"/></td>
            	</tr>
            </table>
            </div>
	            <div class="list">
	                <table>
	                    <thead>
	                        <tr>
	                        	<th>ID</th>
	                        	<th>Data/Hora</th>
								<th>Cod Estab</th>
	                        	<th>Condutor</th>
	                        	<th>Placa</th>
	                        	<th>Hodometro</th>
	                        	<th>Combustível</th>
	                        	<th>Litros</th>
	                        	<th>Preço Unit.</th>
	                        	<th>Valor</th>
	                        	<th>Percorrido(Km)</th>
	                        	<th>Status</th>
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${transacaoInstanceList}" status="i" var="transacaoInstance">
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                            <td>${transacaoInstance.id}</td>
	                            <td>${transacaoInstance?.datahora}</td>
				    			<td>${transacaoInstance.codestab}</td>
	                            <td>${transacaoInstance.condutor}</td>
	                           	<td>${transacaoInstance.placa}</td>
	                           	<td>${transacaoInstance.hodometro}</td>
	                           	<td>${transacaoInstance.combustivel}</td>
	                           	<td>${transacaoInstance.litros}</td>
	                           	<td>${transacaoInstance.preco}</td>
	                           	<td>${transacaoInstance.total}</td>
	                           	<td>${transacaoInstance.percorrido}</td>
	                           	<td>${transacaoInstance.status}</td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
	            </div>
            
            <div class="paginateButtons">
                <g:paginate total="${transacaoInstanceTotal}" params="${pars}"/>
            </div>
            <export:formats formats="['excel', 'pdf']" params="${pars}"/>
        </div>
    </body>
</html>

