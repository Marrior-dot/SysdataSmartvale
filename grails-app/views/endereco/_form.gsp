<fieldset class="uppercase">
	<h2>${legend}</h2>
	
	<div>
		<label><span>CEP</span><g:textField name="${endereco}.cep" class="cep" value="${enderecoInstance?.cep}" size="10"/></label>
		<label><span>Logradouro</span><g:textField name="${endereco}.logradouro" value="${enderecoInstance?.logradouro}" size="50"/></label>
		<label><span>Nº</span><g:textField name="${endereco}.numero" value="${enderecoInstance?.numero}" size="6" maxlength="6"/></label>
		<div class="clear"></div>
	</div>

	<div>
		<label><span>Complemento</span><g:textField name="${endereco}.complemento" value="${enderecoInstance?.complemento}" /></label>
		<label><span>Bairro</span><g:textField name="${endereco}.bairro" value="${enderecoInstance?.bairro}" /></label>
		<div class="clear"></div>
	</div>
	
	<div>
		<label><span>Estado</span>
			<div style="width:150px;padding-bottom:20px;" class="disable">
				<gui:autoComplete
						class="disable"
				        id="estado_${endereco}"
				        controller="estado"
				        action="listAllJSON"
				        name="${endereco}.cidade.estado.nome" 
				        value="${enderecoInstance?.cidade?.estado?.nome}"
				/>	
			</div>
		</label>
	
		<label><span>Cidade</span>
			<div style="width:200px;" class="disable">
				<gui:autoComplete
				        id="cidade_${endereco}"
				        controller="cidade"
				        action="listByEstadoJSON"
				        dependsOn="[value:'estado_'+endereco,useId:true,label:'estId']"
				        name="${endereco}.cidade.nome" 
				        value="${enderecoInstance?.cidade?.nome}"
				/>
			</div>	
		</label>
		<div class="clear"></div>
	</div>
</fieldset>


