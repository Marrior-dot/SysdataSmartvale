<%@ page import="com.sysdata.gestaofrota.Veiculo" %>
<%@ page import="com.sysdata.gestaofrota.MarcaVeiculo" %>


<div class="enter">							
	<g:form method="post" >
		<g:hiddenField name="id" value="${veiculoInstance?.id}" />
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
				<label><span>Placa</span><g:textField name="placa" value="${veiculoInstance?.placa}" size="8" maxlength="8"/></label>
				<label><span>Marca</span><g:select name="marca.id" from="${MarcaVeiculo.list()}" optionKey="id" optionValue="nome" noSelection="${['null':'Selecione uma marca...']}" value="${veiculoInstance?.marca?.id}"/></label>
				<label><span>Modelo</span><g:textField name="modelo" value="${veiculoInstance?.modelo}" size="20" maxlength="20"/></label>
				<label><span>Ano Fabricação</span><g:textField name="anoFabricacao" class="numeric" value="${veiculoInstance?.anoFabricacao}" size="4" maxlength="4"/></label>
				<div class="clear"></div>
			</div>
			<div>
				<label><span>Chassi</span><g:textField name="chassi" value="${veiculoInstance?.chassi}" size="20" maxlength="20" /></label>
				<label><span>Capacidade Tanque (em litros)</span><g:textField name="capacidadeTanque" class="numeric" value="${veiculoInstance?.capacidadeTanque}" size="3" maxlength="3" /></label>
				<label><span>Autonomia (km/l)</span><g:textField name="autonomia" class="numeric" value="${veiculoInstance?.autonomia}" size="3" maxlength="3" /></label>
				<div class="clear"></div>
			</div>	
			<div>
				<label><span>Tipo de Combustível</span><g:select name="tipoAbastecimento" from="${com.sysdata.gestaofrota.TipoAbastecimento?.values()}" value="${veiculoInstance?.tipoAbastecimento}"  /></label>
				<label><span>Validade Extintor</span><gui:datePicker id="validadeExtintor" name="validadeExtintor" value="${veiculoInstance?.validadeExtintor}" formatString="dd/MM/yyyy"/></label>
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