<div class="panel panel-default">

	<div class="panel-heading">${legend}</div>
	<div class="panel-body">

		<div class="row">
            <div class="col-xs-2">
                <bs:formField id="${endereco}.cep" class="cep" name="${endereco}.cep" label="CEP"  value="${enderecoInstance?.cep}" />
            </div>
            <div class="col-xs-8">
                <bs:formField id="${endereco}.logradouro" name="${endereco}.logradouro" label="Logradouro"  value="${enderecoInstance?.logradouro}" />
            </div>
            <div class="col-xs-2">
                <bs:formField id="${endereco}.numero" name="${endereco}.numero" label="Nº"  value="${enderecoInstance?.numero}" />
            </div>
		</div>

        <div class="row">
            <div class="col-xs-12">
                <bs:formField id="${endereco}.complemento" name="${endereco}.complemento" label="Complemento"  value="${enderecoInstance?.complemento}" />
            </div>
        </div>

        <div class="row">
			<div class="col-xs-4">
				<bs:formField id="${endereco}.bairro" name="${endereco}.bairro" label="Bairro"  value="${enderecoInstance?.bairro}" />
			</div>

			<div class="col-xs-4">
				<bs:formField name="${endereco}.cidade.nome" label="Cidade"  value="${enderecoInstance?.cidade?.nome}" />
			</div>

            <div class="col-xs-4">
                <bs:formField name="${endereco}.cidade.estado.nome" label="Estado"  value="${enderecoInstance?.cidade?.estado?.nome}" />
            </div>
        </div>

	</div>
</div>


%{--
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
--}%


