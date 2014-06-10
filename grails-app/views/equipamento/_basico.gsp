<%@ page import="com.sysdata.gestaofrota.Equipamento" %>
<%@ page import="com.sysdata.gestaofrota.TipoEquipamento" %>


<div class="enter">							
	<g:form method="post" >
		<g:hiddenField name="id" value="${equipamentoInstance?.id}" />
		<g:hiddenField name="version" value="${funcionarioInstance?.version}" />
		<g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>     
	    <g:hiddenField name="action" value="${action}" />
	
		<fieldset style="border:1px solid;font-size:14px;">
			<label><span>RH</span>${unidadeInstance?.rh.nome}</label>
			<label><span>Unidade</span>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</label>
			<div class="clear"></div>
		</fieldset>
		
		<fieldset class="uppercase">
			<h2>Dados Básicos</h2>
			<div>
				<label><span>Código</span><g:textField name="codigo" value="${equipamentoInstance?.codigo}" size="10" maxlength="10"/></label>
				<label><span>Descrição</span><g:textField name="descricao" value="${equipamentoInstance?.descricao}" size="60" maxlength="60"/></label>
				<div class="clear"></div>
			</div>
			<div>
				<label><span>Tipo</span><g:select name="tipo.id" from="${TipoEquipamento.list()}" optionKey="id" optionValue="nome" noSelection="${['null':'Selecione um tipo...']}" value="${equipamentoInstance?.tipo?.id}"/></label>
				<label><span>Capacidade Tanque (litros)</span><g:textField name="capacidadeTanque" class="numeric" value="${equipamentoInstance?.capacidadeTanque}" size="5" maxlength="5" /></label>
				<label><span>Média de Consumo (litros/dia)</span><g:textField name="mediaConsumo" value="${equipamentoInstance?.mediaConsumo}" class="numeric" size="5" maxlength="5"/></label>				
				<div class="clear"></div>
			</div>	
			<div>
				<label><span>Tipo de Combustível</span><g:select name="tipoAbastecimento" from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}" value="${equipamentoInstance?.tipoAbastecimento}"  /></label>
				<div class="clear"></div>
			</div>	
		</fieldset>
		
		
	    <div class="buttons">
			<g:if test="${action=='novo'}">
				<span class="button"><g:actionSubmit class="save" action="save" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
			</g:if>
          	<g:if test="${action=='editando'}">
               <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
               <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
          	<g:if test="${action=='visualizando'}">
           	<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
               <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
          	</g:if>
	    </div>
		
	</g:form>
</div>