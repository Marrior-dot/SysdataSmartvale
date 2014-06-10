<%@ page import="com.sysdata.gestaofrota.Banco" %>
<%@ page import="com.sysdata.gestaofrota.TipoTitular" %>

<fieldset class="uppercase">
	<h2>${legend}</h2>
	
	<div>
		<label><span>Banco</span><g:select name="${dadoBancario}.banco.id" 
											value="${dadoBancarioInstance?.banco?.id}" 
											noSelection="${['null':'Selecione o banco...'] }" 
											from="${Banco.list()}"	
											optionKey="id" 
											optionValue="nome"></g:select> </label>
		<label><span>Agência</span><g:textField name="${dadoBancario}.agencia" value="${dadoBancarioInstance?.agencia}" /></label>
		<label><span>Nº Conta</span><g:textField name="${dadoBancario}.conta" value="${dadoBancarioInstance?.conta}" /></label>
		<div class="clear"></div>
	</div>

	<div>
		<label><span>Tipo Titular</span><g:radioGroup name="${dadoBancario}.tipoTitular" value="${dadoBancarioInstance?.tipoTitular}" values="${TipoTitular.asList()}"
											labels="${TipoTitular.asList()*.nome}">
											<p>${it.label} ${it.radio}</p>
										</g:radioGroup> </label>
		<label><span>Titular</span><g:textField name="${dadoBancario}.nomeTitular" value="${dadoBancarioInstance?.nomeTitular}" size="40" maxlength="40"/></label>
		<label><span>Documento(CPF/CNPJ)</span><g:textField name="${dadoBancario}.documentoTitular" value="${dadoBancarioInstance?.documentoTitular}" size="18" maxlength="18"/></label>
	</div>

	
</fieldset>


