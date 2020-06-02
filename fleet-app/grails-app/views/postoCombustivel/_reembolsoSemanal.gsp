<%@ page import="com.sysdata.gestaofrota.DiaSemana" %>

<form id="reembolsoSemanalForm">
	<g:hiddenField name="id" value="${reembolsoInstance?.id}"/>
	<g:hiddenField name="parId" value="${reembolsoInstance?.parId}"/>
	
	<fieldset>
		<h2>Reembolso Semanal</h2>

		<label><span>Dia Semana</span><g:select class="form-control" name="diaSemana" from="${DiaSemana.values()}" optionValue="nome" value="${reembolsoInstance?.diaSemana}"/></label>
		<label><span>Intervalo Dias</span><g:textField name="intervaloDias" class="numeric required form-control" size="2" value="${reembolsoInstance?.intervaloDias}"/></label>
	</fieldset>
</form>



