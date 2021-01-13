<%@ page import="com.sysdata.gestaofrota.DiaSemana" %>


<div class="panel panel-default">

	<div class="panel-heading">Reembolso Semanal</div>

	<div class="panel-body">

		<div class="error"></div>

		<g:form name="reembolsoSemanalForm">

			<g:hiddenField name="id" value="${reembolsoInstance?.id}"/>
			<g:hiddenField name="parId" value="${reembolsoInstance?.parId}"/>

			<div class="row">
				<div class="col-md-4 form-group">
					<label for="diaSemana">Dia Semana</label>
					<g:select class="form-control" name="diaSemana" from="${DiaSemana.values()}" optionValue="nome" value="${reembolsoInstance?.diaSemana}"/>
				</div>
				<div class="col-md-4 form-group">
					<label for="intervaloDias">Intervalo Dias</label>
					<g:field type="number" name="intervaloDias" class="required form-control" value="${reembolsoInstance?.intervaloDias}"/>
				</div>

			</div>
		</g:form>
	</div>
</div>
