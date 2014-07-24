<%@ page import="com.sysdata.gestaofrota.Role" %>
<%@page import="com.sysdata.gestaofrota.Processadora"%>
<%@page import="com.sysdata.gestaofrota.Administradora"%>
<%@page import="com.sysdata.gestaofrota.Rh"%>
<%@page import="com.sysdata.gestaofrota.PostoCombustivel"%>
<%
		
		def l=[:]
		l=ownerList.collect{
					if(it instanceof PostoCombustivel){
						[id:it.id,
						nome:"Estabelecimento - " + it.nome]
					}else if(it instanceof Rh){
						[id:it.id,
						nome:"RH - " + it.nome]
					}else if(it instanceof Administradora){
						[id:it.id,
						nome:"Administradora - " + it.nome]
					}else if(it instanceof Processadora){
						[id:it.id,
						nome:"Processadora - " + it.nome]
					}
				}
		 %>	

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
	<g:select name="owner.id" from="${l}" value="${roleInstance?.owner?.id}" optionKey="id" optionValue="nome"></g:select>
</div>

