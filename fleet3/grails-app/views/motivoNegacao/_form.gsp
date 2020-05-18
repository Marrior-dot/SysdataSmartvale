<%@ page import="com.sysdata.gestaofrota.MotivoNegacao" %>



<div class="fieldcontain ${hasErrors(bean: motivoNegacaoInstance, field: 'codigo', 'error')} ">
	<label for="codigo">
		<g:message code="motivoNegacao.codigo.label" default="Codigo" />
		
	</label>
	<g:textField name="codigo" class="form-control" value="${motivoNegacaoInstance?.codigo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: motivoNegacaoInstance, field: 'descricao', 'error')} ">
	<label for="descricao">
		<g:message code="motivoNegacao.descricao.label" default="Descricao" />
		
	</label>
	<g:textField name="descricao" class="form-control" value="${motivoNegacaoInstance?.descricao}"/>
</div>
<br><br>

