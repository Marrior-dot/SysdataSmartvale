<fieldset>
<%--	<input type="radio" name="opcao" value="1" checked="true">Matrícula</input> --%>
<%--	<input type="radio" name="opcao" value="2">Nome</input>--%>
<%--	<input type="radio" name="opcao" value="3">CPF</input>--%>
<%--	<br><br>--%>
<%--	<label>Filtro: <g:textField name="filtro" value="${filtro}"/></label>--%>
</fieldset>


<gui:dataTable 
			id="pdCrgSearchDT"
			controller="pedidoCarga" action="listAllJSON"
			params="[unidade_id:unidade_id]"
			columnDefs="[
				[key:'id',sortable:true,resizeable:true,label:'ID'],
				[key:'rh',sortable:true,resizeable:true,label:'RH'],
				[key:'unidade',sortable:true,resizeable:true,label:'Unidade'],
				[key:'dataPedido',sortable:true,resizeable:true,label:'Data Pedido'],
				[key:'dataCarga',sortable:true,resizeable:true,label:'Data Carga'],
				[key:'total',sortable:true,resizeable:true,label:'Total'],
				[key:'status',sortable:true,resizeable:true,label:'Status'],
				[key:'acoes',sortable:true,resizeable:true,label:'Ações']
			]"
			sortedBy="dataPedido"
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
	
	$('input[name="filtro"]').keyup(function(){
	
		var params='';
		
		<g:if test="${unidade_id}">
			params+='unidade_id='+${unidade_id};
		</g:if>
		
		params+="opcao="+$(':checked').val()+"&filtro="+$(this).val(); 
	

		filtrarEntidade(GRAILSUI.pdCrgSearchDT,params);
		
	});

</script>
	


