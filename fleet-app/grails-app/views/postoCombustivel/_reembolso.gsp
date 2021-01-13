<div class="panel panel-default">

	<div class="panel-heading">Reembolso Intervalos Múltiplos</div>

	<div class="panel-body">

		<div class="error"></div>

		<g:form name="reembolsoForm">
			<g:hiddenField name="id" value="${reembolsoInstance?.id}"/>
			<g:hiddenField name="parId" value="${reembolsoInstance?.parId}"/>

			<div class="row">

				<div class="col-md-3">
					<label for="inicioIntervalo">Dia Início</label>
					<g:textField  name="inicioIntervalo" class="numeric mandatory form-control" size="2" maxlength="2"
								  value="${reembolsoInstance?.inicioIntervalo}"/>
				</div>
				<div class="col-md-3">
					<label for="fimIntervalo">Dia Fim</label>
					<g:textField name="fimIntervalo" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.fimIntervalo}"/>
				</div>

				<div class="col-md-3">
					<label for="diaEfetivacao">Dia Reembolso</label>
					<g:textField name="diaEfetivacao" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.diaEfetivacao}"/>
				</div>

				<div class="col-md-3">
					<label for="meses">Meses</label>
					<g:textField name="meses" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.meses}"/>
				</div>

			</div>

		</g:form>

	</div>
</div>
