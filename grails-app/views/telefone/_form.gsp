<fieldset>
	<h2>${legend}</h2>
	<label><span>DDD</span><g:textField name="${telefone}.ddd" value="${telefoneInstance?.ddd}" style="width:30px" maxlength="2"/></label>
	<label><span>Número</span><g:textField name="${telefone}.numero" value="${telefoneInstance?.numero}" style="width:100px" maxlength="9"/></label>
	<label><span>Ramal</span><g:textField name="${telefone}.ramal" value="${telefoneInstance?.ramal}" style="width:60px" maxlength="4" /></label>
</fieldset>


