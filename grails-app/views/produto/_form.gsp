<%@ page import="com.sysdata.gestaofrota.Produto" %>
<div class="panel panel-default">
	<div class="panel-heading">Dados Básicos</div>

	<div class="panel-body">
		<div class="row">
			<div class="form-group col-md-4">
				<label for="codigo">
					<g:message code="produto.codigo.label" default="Codigo" />

				</label>
				<g:textField class="form-control" name="codigo" value="${produtoInstance?.codigo}"/>
			</div>

			<div class="form-group col-md-4">
				<label for="nome">
					<g:message code="produto.nome.label" default="Nome" />

				</label>
				<g:textField class="form-control" name="nome" value="${produtoInstance?.nome}"/>
			</div>

			<div class="form-group col-md-4">
				<label for="tipo">
					<g:message code="produto.tipo.label" default="Tipo" />

				</label>
				<g:select class="form-control" name="tipo" from="${com.sysdata.gestaofrota.TipoProduto?.values()}" keys="${com.sysdata.gestaofrota.TipoProduto.values()*.name()}" value="${produtoInstance?.tipo?.name()}" noSelection="['': '']"/>
			</div>
		</div>

	</div>
</div>


