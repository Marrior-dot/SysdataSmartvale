<jq:jquery>

	function filtrarCategorias(filtro){

		var params="rhId=${rhId}&opcao="+$("input[name='opcaoCat']:checked").val()+"&filtro="+filtro;
		
		filtrarEntidade(GRAILSUI.categSearchDT,params);
	
	}

	//Filtra enquanto digita
	$('input[name="filtroCateg"]').keyup(function(){
		filtrarCategorias($(this).val());
	});
</jq:jquery>

<g:form controller="${controller}">

		<div class="buttons">
			<span class="button"><g:actionSubmit class="new" action="create" value="${message(code:'default.new.label', args:[message(code:'categoriaFuncionario.label')]) }"/></span>
		</div>
	
		<fieldset class="search">
			<input type="hidden" id="categId" name="categId"/>
			<input type="hidden" name="rhId" value="${rhId}"/>
			
			<input type="radio" name="opcaoCat" value="1" checked="true">Código</input> 
			<input type="radio" name="opcaoCat" value="2">Nome</input>
			<br><br>
			<label>Filtro: <g:textField name="filtroCateg" value="${filtro}"/></label>
		</fieldset>
		
		<gui:dataTable 
					id="categSearchDT"
					controller="categoriaFuncionario" action="listAllJSON"
					columnDefs="[
						[key:'id',sortable:true,resizeable:true,label:'Código'],
						[key:'nome',sortable:true,resizeable:true,label:'Nome'],
						[key:'valor',sortable:true,resizeable:true,label:'Valor Carga'],
						[key:'acoes',label:'Ações']
					]"
					sortedBy="id"
					rowsPerPage="10"
					params="[rhId:rhId]"
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






