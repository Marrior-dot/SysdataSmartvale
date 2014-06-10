<g:form controller="${controller}">

	 <div class="buttons">
	  <span class="button"><g:actionSubmit class="new" action="create" 	value="${message(code:'default.new.label', args:[message(code:'user.label')]) }"/></span>
	 </div>


	<fieldset class="search">
		<input type="radio" name="opcao" value="1" checked="true">Nome</input> 
		<input type="radio" name="opcao" value="2">Login</input>
		<br><br>
		<label>Filtro: <g:textField name="filtroUser" value="${filtro}"/></label>
	</fieldset>
	
		<gui:dataTable 
	 			id="userSearchDT"
	 			controller="user" action="listAllJSON"
	 			columnDefs="[
	 				[key:'id',hidden:true],
	 				[key:'name',sortable:true,resizeable:true,label:'Nome'],
	 				[key:'login',sortable:true,resizeable:true,label:'Login'],
	 				[key:'owner',sortable:true,resizeable:true,label:'Organização'],
	 				[key:'action',label:'Ação']
	 			]"
	 			sortedBy="name"
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
	$('input[name="filtroUser"]').keyup(function(){
		filtrarUsuarios($(this).val());
	});
	
	
	function filtrarUsuarios(filtro){

		var params='';
		
		params+="opcao="+$(':checked').val()+"&filtro="+filtro;
		
		filtrarEntidade(GRAILSUI.userSearchDT,params); 
		
	}
		
</jq:jquery>
