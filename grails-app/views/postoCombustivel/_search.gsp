<fieldset class="search">
	<h2>Pesquisa por filtro</h2>

	<input type="hidden" id="selectedId" name="selectedId"/>
	<input type="radio" name="opcao" value="1" checked="true">Nome Fantasia</input> 
	<input type="radio" name="opcao" value="2">CNPJ</input>
<%--	<input type="radio" name="opcao" value="3">Cod.Estabelecimento</input>--%>
	<br><br>
	<label>Filtro: <g:textField name="filtro" value="${filtro}"/></label>
</fieldset>


<gui:dataTable 
			id="postoSearchDT"
			controller="postoCombustivel" action="listAllJSON"
			columnDefs="[
				[key:'id',hidden:true],
				[key:'razao',sortable:true,resizeable:true,label:'Razão Social'],
				[key:'nomeFantasia',sortable:true,resizeable:true,label:'Nome Fantasia'],
				[key:'cnpj',sortable:true,resizeable:true,label:'CNPJ'],
				[key:'acao',label:'Ações'],
			]"
			sortedBy="razao"
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
 			
<jq:jquery>
	
	$('input[name="filtro"]').keyup(function(){

		var params='';
		
		<g:if test="${unidade_id}">
			params+='unidade_id='+${unidade_id};
		</g:if>
		
		params+="opcao="+$(':checked').val()+"&filtro="+$(this).val();
		
		filtrarEntidade(GRAILSUI.postoSearchDT,params);
		
	});
</jq:jquery>


