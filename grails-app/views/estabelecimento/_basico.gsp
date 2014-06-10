<div class="enter">
	<div class="dialog">
		<fieldset class="uppercase">
			<h2>Dados Básicos</h2>
			<label><span>CNPJ</span><g:textField name="cnpj" value="${estabelecimentoInstance?.cnpj}" /></label>
			<label><span>Razão Social</span><g:textField name="nome" value="${estabelecimentoInstance?.nome}" size="50" maxlength="50"/></label>
			<div>
				<label><span>Nome Fantasia</span><g:textField name="nomeFantasia" value="${estabelecimentoInstance?.nomeFantasia}" size="50" maxlength="50"/></label>
				<div class="clear"></div>
			</div>
			<label><span>Inscrição Estadual</span><g:textField name="inscricaoEstadual" value="${estabelecimentoInstance?.inscricaoEstadual}" class="numeric" size="10" maxlength="10"/></label>
			<label><span>Inscrição Municipal</span><g:textField name="inscricaoMunicipal" value="${estabelecimentoInstance?.inscricaoMunicipal}" class="numeric" size="10" maxlength="10"/></label>
		</fieldset>
		        
		<g:render template="/endereco/form" model="[enderecoInstance:estabelecimentoInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>
		
		<g:render template="/telefone/form" model="[telefoneInstance:estabelecimentoInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>
			
	</div>
</div>

