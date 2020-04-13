<%@ page import="com.sysdata.gestaofrota.Role" %>

<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'authority', 'error')} required">
	<label for="authority">
		<g:message code="role.authority.label" default="Authority" />
	</label>
	<span class="required-indicator">*</span>
	<g:textField name="authority" required="" value="${roleInstance?.authority}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: roleInstance, field: 'owner', 'error')} required">
	<label for="owner">
		<g:message code="role.owner.label" default="Owner" />
	</label>
	<span class="required-indicator">*</span>
	<g:select name="owner" from="${com.sysdata.gestaofrota.Role.listOwners()}" value="${roleInstance?.owner}"></g:select>
</div>

