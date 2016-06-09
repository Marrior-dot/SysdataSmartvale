<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>



<div class="fieldcontain ${hasErrors(bean: tipoEquipamentoInstance, field: 'nome', 'error')} ">
	<label for="nome">
		<g:message code="tipoEquipamento.nome.label" default="Nome" />
		
	</label>
	<g:textField name="nome" class="form-control" value="${tipoEquipamentoInstance?.nome}"/>
</div>
<br><br>

