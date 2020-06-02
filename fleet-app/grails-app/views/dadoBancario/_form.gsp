<%@ page import="com.sysdata.gestaofrota.Banco" %>
<%@ page import="com.sysdata.gestaofrota.TipoTitular" %>


<div class="panel panel-default">
	<div class="panel-heading">${legend}</div>
	<div class="panel-body">
		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label for="${dadoBancario}.banco.id">Banco</label>
					<g:select name="${dadoBancario}.banco.id" class="form-control"
							  value="${dadoBancarioInstance?.banco?.id}"
							  noSelection="${['null':'Selecione o banco...']}"
							  from="${Banco.list()}" optionKey="id" optionValue="nome"/>
				</div>
			</div>

			<div class="col-xs-4">
				<bs:formField id="${dadoBancario}.agencia" name="${dadoBancario}.agencia" label="Agência" value="${dadoBancarioInstance?.agencia}"/>
			</div>

			<div class="col-xs-4">
				<bs:formField id="${dadoBancario}.conta" name="${dadoBancario}.conta" label="Nº Conta" value="${dadoBancarioInstance?.conta}"/>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-4">
				<div class="form-group">
					<label for="${dadoBancario}.tipoTitular">Tipo Titular</label>
					<g:select name="${dadoBancario}.tipoTitular" from="${TipoTitular.asList()}" class="form-control"
						value="${dadoBancarioInstance?.tipoTitular}" optionValue="nome"/>
				</div>
			</div>
			<div class="col-xs-4">
				<bs:formField id="${dadoBancario}.nomeTitular" name="${dadoBancario}.nomeTitular" label="Titular" value="${dadoBancarioInstance?.nomeTitular}" maxlength="40"/>
			</div>
			<div class="col-xs-4">
				<bs:formField id="${dadoBancario}.documentoTitular" name="${dadoBancario}.documentoTitular" label="Documento(CPF/CNPJ)" value="${dadoBancarioInstance?.documentoTitular}" maxlength="18"/>
			</div>
		</div>
	</div>
</div>


