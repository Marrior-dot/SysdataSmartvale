<jq:jquery>

	function filtrarUnidades(filtro){

		var params="rhId=${rhId}&opcao="+$("input[name='opcaoUnid']:checked").val()+"&filtro="+filtro; 
	
		filtrarEntidade(GRAILSUI.unidSearchDT,params);
	
	}
	

	//Filtra enquanto digita
	$('input[name="filtro"]').keyup(function(){
		filtrarUnidades($(this).val());
	});
</jq:jquery>

<g:form controller="${controller}">

		<div class="buttons">
			<span class="button"><g:actionSubmit class="new" action="create" value="${message(code:'default.new.label', args:[message(code:'unidade.label')]) }"/></span>
		</div>

	
		<fieldset class="search">
			<input type="hidden" id="unidId" name="unidId"/>
			<input type="hidden" name="rhId" value="${rhId}"/>
			
			<input type="radio" name="opcaoUnid" value="1" checked="true">Código</input> 
			<input type="radio" name="opcaoUnid" value="2">Nome</input>
			<br><br>
			<label>Filtro: <g:textField name="filtro" value="${filtro}"/></label>
		</fieldset>
		
		<gui:dataTable 
					id="unidSearchDT"
					controller="${controller}" action="listAllJSON"
					columnDefs="[
						[key:'id',sortable:true,resizeable:true,label:'Código'],
						[key:'nome',sortable:true,resizeable:true,label:'Nome'],
						[key:'status',sortable:true,resizeable:true,label:'Status'],
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






