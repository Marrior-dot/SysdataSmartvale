<fieldset class="search">

	<h2>Pesquisa por filtro</h2>

	<input type="hidden" id="rhId" name="rhId"/>
	<input type="radio" name="opcao" value="1" checked="true">Código</input> 
	<input type="radio" name="opcao" value="2">Nome Fantasia</input>
	<input type="radio" name="opcao" value="3">CNPJ</input>
	<br><br>
	<label>Filtro: <g:textField name="filtroRh" value="${filtro}"/></label>
</fieldset>


<gui:dataTable 
			id="rhSearchDT"
			controller="rh" action="listAllJSON"
			columnDefs="[
				[key:'id',hidden:true],
				[key:'codigo',sortable:true,resizeable:true,label:'Código'],
				[key:'razao',sortable:true,resizeable:true,label:'Razão Social'],
				[key:'fantasia',sortable:true,resizeable:true,label:'Nome Fantasia'],
				[key:'cnpj',sortable:true,resizeable:true,label:'CNPJ'],
				[key:'acao',label:'Ação']
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

<script type="text/javascript">

	function filtrarRhs(filtro){

		var params="opcao="+$(':checked').val()+"&filtro="+filtro;

		filtrarEntidade(GRAILSUI.rhSearchDT,params);


	}

	//Filtra enquanto digita
	$('input[name="filtroRh"]').keyup(function(){
		filtrarRhs($(this).val());
	});

</script>



