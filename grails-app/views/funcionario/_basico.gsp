<%@ page import="com.sysdata.gestaofrota.CategoriaFuncionario" %>
<%@ page import="com.sysdata.gestaofrota.Status" %>
<%@ page import="com.sysdata.gestaofrota.CategoriaCnh" %>

<%@ page import="com.sysdata.gestaofrota.Util" %>


<style type="text/css">
	
	#lblGestor{
		margin-left:2em;
	}
	
</style>



            <g:form method="post" >
                <g:hiddenField name="id" value="${funcionarioInstance?.id}" />
                <g:hiddenField name="version" value="${funcionarioInstance?.version}" />
				<g:hiddenField name="unidId" value="${unidadeInstance?.id}"/>                
                <g:hiddenField name="action" value="${action}"/>
                <div class="dialog">
                
                <fieldset style="border:1px solid;font-size:14px;">
					<label><span>${message(code: 'rh.label', default: 'RH')}</span>${unidadeInstance?.rh.nome}</label>
					<label><span>${message(code: 'unidade.label', default: 'Unidade')}</span>${unidadeInstance?.codigo}-${unidadeInstance?.nome}</label>
					<div class="clear"></div>
                </fieldset>

				<fieldset class="uppercase">
					<h2>Dados Básicos</h2>
					<div>
						<label><span>Matrícula</span><g:textField name="matricula" value="${funcionarioInstance?.matricula}" size="10" maxlength="10"/></label>
						<label><span>CPF</span><g:textField name="cpf" value="${funcionarioInstance?.cpf}" /></label>
						<label><span>RG</span><g:textField name="rg" class="numeric" value="${funcionarioInstance?.rg}" size="10" maxlength="10"/></label>
						<div class="clear"></div>
					</div>
					<div>
						<label><span>Nome</span><g:textField name="nome" value="${funcionarioInstance?.nome}" size="50" maxlength="50" /></label>
						<label><span>Data Nascimento</span><gui:datePicker id="dataNascimento" name="dataNascimento" value="${funcionarioInstance?.dataNascimento}" formatString="dd/MM/yyyy"/></label>
						<div class="clear"></div>
					</div>	
					<div>
						<label><span>CNH</span><g:textField name="cnh" class="numeric" value="${funcionarioInstance?.cnh}" size="11" maxlength="11" /></label>
						<label><span>Validade CNH</span><gui:datePicker id="validadeCnh" name="validadeCnh" value="${funcionarioInstance?.validadeCnh}" formatString="dd/MM/yyyy"/></label>
						<label><span>Categoria CNH</span><g:select name="categoriaCnh" from="${CategoriaCnh.values()}" optionValue="nome" noSelection="${['null':'Selecione uma Categ. CNH...']}" value="${funcionarioInstance?.categoriaCnh}"/></label>
						
						<div class="clear"></div>
					</div>	
					
					
					<div>
					
						<label><span>Categoria</span><g:select name="categoria.id" 
										value="${funcionarioInstance?.categoria?.id}" 
										noSelection="${['null':'Selecione a categoria...'] }" 
										from="${CategoriaFuncionario.withCriteria{rh{eq('id',unidadeInstance?.rh?.id)}}}"	
										optionKey="id" 
										optionValue="nome"></g:select> </label>
										
						<label id="lblGestor"><span>Gestor</span><g:checkBox name="gestor" value="${funcionarioInstance?.gestor}"/></label>
						
					</div>
					
					<div>
					
						<label><span>Status</span><g:select name="status" from="${Status.asBloqueado()}" value="${funcionarioInstance?.status}" optionKey="" optionValue=""></g:select></label>
					
					</div>
					
				</fieldset>
				        
				<g:render template="/endereco/form" model="[enderecoInstance:funcionarioInstance?.endereco,endereco:'endereco',legend:'Endereço Residencial']"/>
				
				<div style="float:left">
					<g:render template="/telefone/form" model="[telefoneInstance:funcionarioInstance?.telefone,telefone:'telefone',legend:'Telefone Residencial']"/>
				</div>
					<g:render template="/telefone/form" model="[telefoneInstance:funcionarioInstance?.telefoneComercial,telefone:'telefoneComercial',legend:'Telefone Comercial']"/>
                </div>
                
					<div class="buttons">
						<g:if test="${action in [Util.ACTION_NEW,Util.ACTION_EDIT]}">
							<span class="button"><g:actionSubmit class="save" action="${action==Util.ACTION_NEW?'save':'update'}" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
						</g:if>
						<g:if test="${action==Util.ACTION_VIEW}">
							<span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
							<span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
						</g:if>
					</div>
                
            </g:form>
