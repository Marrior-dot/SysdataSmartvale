<g:form controller="${controller}">

		 <div class="buttons">
		  <span class="button"><g:actionSubmit class="new" 
		  						action="${unidade_id?'create':'selectRhUnidade'}" 
		  						value="${message(code:'default.new.label', args:[message(code:'equipamento.label')]) }"/></span>
		 </div>



	<fieldset class="search">
		<h2>Pesquisa por filtro</h2>
	
		<input type="hidden" id="equipId" name="equipId"/>
		<input type="hidden" name="unidade_id" value="${unidade_id}"/>

		<input type="radio" name="opcao" value="1" checked="true">Código</input> 
		<input type="radio" name="opcao" value="2">Descrição</input>
		<br><br>
		<label>Filtro: <g:textField name="filtroEquip" value="${filtro}"/></label>
	</fieldset>
	
		<gui:dataTable 
	 			id="equipSearchDT"
	 			controller="equipamento" action="listAllJSON"
	 			params="[unidade_id:unidade_id]"
	 			columnDefs="[
	 				[key:'id',hidden:true],
	 				[key:'codigo',sortable:true,resizeable:true,label:'Código'],
	 				[key:'descricao',sortable:true,resizeable:true,label:'Descrição'],
	 				[key:'tipo',sortable:true,resizeable:true,label:'Tipo'],
	 				[key:'acao',label:'Ação']
	 			]"
	 			sortedBy="codigo"
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
	$('input[name="filtroEquip"]').keyup(function(){
		filtrarEquipamentos($(this).val());
	});
	
	
	function filtrarEquipamentos(filtro){

		var params='';
		
		<g:if test="${unidade_id}">
			params+="unidade_id=${unidade_id}&";
		</g:if>
		
		params+="opcao="+$(':checked').val()+"&filtro="+filtro;
		
		filtrarEntidade(GRAILSUI.equipSearchDT,params); 
		
	}
		
</jq:jquery>
