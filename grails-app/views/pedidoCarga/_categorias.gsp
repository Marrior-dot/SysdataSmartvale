<gui:dataTable 
			id="categoriasDT"
			controller="categoriaFuncionario" action="listAllJSON"
			params="[rhId:unidadeInstance?.rh?.id]"
			columnDefs="[
				[key:'id',hidden:true],
				[key:'codigo',sortable:true,resizeable:true,label:'Código'],
				[key:'nome',sortable:true,resizeable:true,label:'Nome'],
				[key:'valor',sortable:true,resizeable:true,label:'Valor Carga']
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
 			
<script type="text/javascript">

	var countCat={};
	var totalCat={};

	YAHOO.util.Event.onDOMReady(function(){

		/* No carregamento da listagem de Categorias:
			1. Seleciona a primeira categoria da tabela (primeira linha);
		*/
		GRAILSUI.categoriasDT.subscribe("initEvent",function(args){

			var firstRow=GRAILSUI.categoriasDT.getRecordSet().getRecords()[0];
			GRAILSUI.categoriasDT.selectRow(firstRow);
						
		});
		
		/* Na seleção de uma linha da tabela de Categorias:
		*	1. Filtra os funcionários que aparecerão na tabela Funcionários a partir da categoria selecionada aqui;
		*/   
		GRAILSUI.categoriasDT.subscribe("rowSelectEvent",function(args){

			var selected=GRAILSUI.categoriasDT.getRecord(GRAILSUI.categoriasDT.getSelectedRows()[0]);
			filtrarFuncionarios(selected.getData('id'),null);

		});
	});

</script>


	


