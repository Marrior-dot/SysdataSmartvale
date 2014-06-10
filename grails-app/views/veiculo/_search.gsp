


<g:form controller="${controller}">

		 <div class="buttons">
		  <span class="button"><g:actionSubmit class="new" 
		  						action="${unidade_id?'create':'selectRhUnidade'}" 
		  						value="${message(code:'default.new.label', args:[message(code:'veiculo.label')]) }"/></span>
		 </div>



	<fieldset class="search">
		<h2>Pesquisa por filtro</h2>
	
		<input type="hidden" id="veicId" name="veicId"/>
		<input type="hidden" name="unidade_id" value="${unidade_id}"/>

		<input type="radio" name="opcao" value="1" checked="true">Placa</input> 
		<input type="radio" name="opcao" value="2">Modelo</input>
		<br><br>
		<label>Filtro: <g:textField name="filtroVeic" value="${filtro}"/></label>
	</fieldset>
	
		<gui:dataTable 
	 			id="veicSearchDT"
	 			controller="veiculo" action="listAllJSON"
	 			params="[unidade_id:unidade_id]"
	 			columnDefs="[
	 				[key:'id',hidden:true],
	 				[key:'placa',sortable:true,resizeable:true,label:'Placa'],
	 				[key:'marca',sortable:true,resizeable:true,label:'Marca'],
	 				[key:'modelo',sortable:true,resizeable:true,label:'Modelo'],
	 				[key:'ano',sortable:true,resizeable:true,label:'Ano'],
	 				[key:'acao',label:'Ação']
	 			]"
	 			sortedBy="marca"
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
	
	//Filtra enquanto digita
	$('input[name="filtroVeic"]').keyup(function(){
		filtrarVeiculos($(this).val());
	});
	
	
	function filtrarVeiculos(filtro){

		var params='';
		
		<g:if test="${unidade_id}">
			params+="unidade_id=${unidade_id}&";
		</g:if>
		
		params+="opcao="+$(':checked').val()+"&filtro="+filtro;
		
		filtrarEntidade(GRAILSUI.veicSearchDT,params); 
		
	}
		
</jq:jquery>
