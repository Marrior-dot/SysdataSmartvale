<form id="reembolsoForm">
	<g:hiddenField name="id" value="${reembolsoInstance?.id}"/>
	<g:hiddenField name="parId" value="${reembolsoInstance?.parId}"/>
	
	<fieldset>
		<h2>Reembolso</h2>
		<label><span>Dia Início</span><g:textField  name="inicioIntervalo" class="numeric mandatory form-control" size="2" maxlength="2" value="${reembolsoInstance?.inicioIntervalo}"/></label>
		<label><span>Dia Fim</span><g:textField name="fimIntervalo" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.fimIntervalo}"/></label>
		<label><span>Dia Reembolso</span><g:textField name="diaEfetivacao" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.diaEfetivacao}"/></label>
		<label><span>Meses</span><g:textField name="meses" class="numeric mandatory form-control" size="2" value="${reembolsoInstance?.meses}"/></label>
	</fieldset>
</form>



