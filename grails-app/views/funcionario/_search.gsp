<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Status" %>


<g:form controller="${controller}">


		<g:if test="${!action || action==Util.ACTION_VIEW}">
			<div class="buttons">
				<span class="button"><g:actionSubmit class="new" 
										action="${unidade_id?'create':'selectRhUnidade'}" 
										value="${message(code:'default.new.label', args:[message(code:'funcionario.label')]) }"/></span>
			</div>
		</g:if>




	<fieldset class="search">
		<h2>Pesquisa por filtro</h2>
	
		<input type="hidden" name="unidade_id" value="${unidade_id}"/>
		<input type="radio" name="opcao" value="1" checked="true">Matrícula</input>
		<input type="radio" name="opcao" value="2">Nome</input>
		<input type="radio" name="opcao" value="3">CPF</input>
		<br>

		<label>Filtro: <g:textField name="filtroFunc" value="${filtro}"/></label>
		<label>Status: <g:select name="filtroStatus" from="${Status.asBloqueado()}" value="${status}" optionValue="nome"></g:select></label>

	</fieldset>
	


	<gui:dataTable 
				id="funcSearchDT"
				controller="funcionario" action="listAllJSON"
				params='[unidade_id:unidade_id,gestor:gestor]'
				columnDefs="[
					[key:'id',hidden:true],
					[key:'matricula',sortable:true,resizeable:true,label:'Matrícula'],
					[key:'nome',sortable:true,resizeable:true,label:'Nome'],
					[key:'cpf',sortable:true,resizeable:true,label:'CPF'],
					[key:'acao',label:'Ação']
				]"
				sortedBy="nome"
				rowsPerPage="10"
				paginatorConfig="[
					nextPageLinkLabel:'Prox',
				previousPageLinkLabel:'Ant',
				firstPageLinkLabel:'Prim',
				lastPageLinkLabel:'Ult',
					template:'{FirstPageLink} {PreviousPageLink}  {PageLinks} {NextPageLink} {LastPageLink} {CurrentPageReport}',
					pageReportTemplate:'{totalRecords} total de registros'
				]"
				/>


	
</g:form>


<jq:jquery>

	function filtrarFuncionarios(filtro,categId){
		var params='';
		
		<g:if test="${unidade_id}">
			params+="unidade_id=${unidade_id}&";
		</g:if>
		
		if(categId)
			params+='categId='+categId+'&';
		
		params+="opcao="+$(':checked').val()+"&filtro="+filtro;
		params+="&status="+$('select[name="filtroStatus"]').val();
		
		filtrarEntidade(GRAILSUI.funcSearchDT,params);

	}

	function filtrarStatus(status,categId){
		var params='';

		<g:if test="${unidade_id}">
			params+="unidade_id=${unidade_id}&";
		</g:if>

		if(categId)
	        params+='categId='+categId+'&';

    	params+="status="+status;

    filtrarEntidade(GRAILSUI.funcSearchDT,params);
}

//Filtra enquanto digita

		$('input[name="filtroFunc"]').keyup(function(){
			filtrarFuncionarios($(this).val(),null);
		});



//Filtra se tiver mudança do select
		$('select[name="filtroStatus"]').change(function() {
			filtrarStatus($(this).val(),null);
		 });


</jq:jquery>


	


