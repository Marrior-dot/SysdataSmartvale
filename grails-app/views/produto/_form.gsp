<%@ page import="com.sysdata.gestaofrota.Produto" %>

<div>
	<label for="codigo">
		<g:message code="produto.codigo.label" default="Codigo" />
		
	</label>
	<g:textField name="codigo" value="${produtoInstance?.codigo}"/>
</div>

<div>
	<label for="nome">
		<g:message code="produto.nome.label" default="Nome" />
		
	</label>
	<g:textField name="nome" value="${produtoInstance?.nome}"/>
</div>

<div>
	<label for="tipo">
		<g:message code="produto.tipo.label" default="Tipo" />
		
	</label>
	<g:select name="tipo" from="${com.sysdata.gestaofrota.TipoProduto?.values()}" keys="${com.sysdata.gestaofrota.TipoProduto.values()*.name()}" value="${produtoInstance?.tipo?.name()}" noSelection="['': '']"/>
</div>

