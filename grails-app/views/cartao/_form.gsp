<%@ page import="com.sysdata.gestaofrota.Cartao" %>



<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'numero', 'error')} ">
	<label for="numero">
		<g:message code="cartao.numero.label" default="Numero" />
		
	</label>
	<g:textField name="numero" value="${cartaoInstance?.numero}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'motivoCancelamento', 'error')} ">
	<label for="motivoCancelamento">
		<g:message code="cartao.motivoCancelamento.label" default="Motivo Cancelamento" />
		
	</label>
	<g:select name="motivoCancelamento" from="${com.sysdata.gestaofrota.MotivoCancelamento?.values()}" keys="${com.sysdata.gestaofrota.MotivoCancelamento.values()*.name()}" value="${cartaoInstance?.motivoCancelamento?.name()}" noSelection="['': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'arquivo', 'error')} ">
	<label for="arquivo">
		<g:message code="cartao.arquivo.label" default="Arquivo" />
		
	</label>
	<g:select id="arquivo" name="arquivo.id" from="${com.sysdata.gestaofrota.Arquivo.list()}" optionKey="id" value="${cartaoInstance?.arquivo?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'cvv', 'error')} required">
	<label for="cvv">
		<g:message code="cartao.cvv.label" default="Cvv" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="cvv" maxlength="3" required="" value="${cartaoInstance?.cvv}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'via', 'error')} required">
	<label for="via">
		<g:message code="cartao.via.label" default="Via" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="via" required="" value="${cartaoInstance.via}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'portador', 'error')} required">
	<label for="portador">
		<g:message code="cartao.portador.label" default="Portador" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="portador" name="portador.id" from="${com.sysdata.gestaofrota.Portador.list()}" optionKey="id" required="" value="${cartaoInstance?.portador?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'senha', 'error')} ">
	<label for="senha">
		<g:message code="cartao.senha.label" default="Senha" />
		
	</label>
	<g:textField name="senha" value="${cartaoInstance?.senha}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="cartao.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="status" from="${com.sysdata.gestaofrota.StatusCartao?.values()}" keys="${com.sysdata.gestaofrota.StatusCartao.values()*.name()}" required="" value="${cartaoInstance?.status?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: cartaoInstance, field: 'validade', 'error')} required">
	<label for="validade">
		<g:message code="cartao.validade.label" default="Validade" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="validade" precision="day"  value="${cartaoInstance?.validade}"  />
</div>

