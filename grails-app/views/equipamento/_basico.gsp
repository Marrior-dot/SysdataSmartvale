<%@ page import="com.sysdata.gestaofrota.Equipamento" %>
<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>



<div class="enter">							
	<g:form method="post" >
		<g:hiddenField name="id" value="${equipamentoInstance?.id}" />
		<g:hiddenField name="version" value="${funcionarioInstance?.version}" />
		<g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>     
	    <g:hiddenField name="action" value="${action}" />

		<table class="table" style="border:1px solid;border-color: #DDD;font-size:14px;">
			<thead>
			<tr>
				<th>${message(code: 'rh.label', default: 'RH')}</th>
				<th>${message(code: 'unidade.label', default: 'Unidade')}</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td>${unidadeInstance?.rh.nome}</td>
				<td>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</td>
			</tr>
			</tbody>
		</table>
		
		<fieldset class="uppercase">

			<div>
				<label><span>Código</span><g:textField class="form-control" name="codigo" value="${equipamentoInstance?.codigo}" size="10" maxlength="10"/></label>
				<label><span>Descrição</span><g:textField class="form-control" name="descricao" value="${equipamentoInstance?.descricao}" size="60" maxlength="60"/></label>
				<div class="clear"></div>
			</div>
			<div>
				<label><span>Tipo</span><g:select name="tipo.id" class="form-control" from="${TipoEquipamento.list()}" optionKey="id" optionValue="nome" noSelection="${['null':'Selecione um tipo...']}" value="${equipamentoInstance?.tipo?.id}"/></label>
				<label><span>Capacidade Tanque (litros)</span><g:textField  name="capacidadeTanque" class="numeric form-control" value="${equipamentoInstance?.capacidadeTanque}" size="5" maxlength="5" /></label>
				<label><span>Média de Consumo (litros/dia)</span><g:textField  name="mediaConsumo" value="${equipamentoInstance?.mediaConsumo}" class="numeric form-control" size="5" maxlength="5"/></label>
				<div class="clear"></div>
			</div>	
			<div>
				<label><span>Tipo de Combustível</span><g:select class="form-control" name="tipoAbastecimento" from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}" value="${equipamentoInstance?.tipoAbastecimento}"  /></label>
				<div class="clear"></div>
			</div>	
		</fieldset>
		<br><br>
		
	    <div class="buttons">
			<g:if test="${action=='novo'}">
				<span class="button"><g:actionSubmit class="btn btn-default" action="save" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
			</g:if>
          	<g:if test="${action=='editando'}">
               <span class="button"><g:actionSubmit class="btn btn-default"action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
               <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
          	<g:if test="${action=='visualizando'}">
           	<span class="button"><g:actionSubmit class="btn btn-default" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
               <span class="button"><g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
	    </div>
		
	</g:form>
</div>