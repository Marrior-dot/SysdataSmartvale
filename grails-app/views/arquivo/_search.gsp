<%@ page import="com.sysdata.gestaofrota.TipoArquivo" %>

<fieldset class="search">
	<h2>Filtro de Pesquisa</h2>
	
	<label><span>Data Início</span><gui:datePicker id="dataInicio" name="dataInicio" value="${dataInicio}" formatString="dd/MM/yyyy"/></label>
	<label><span>Data Fim</span><gui:datePicker id="dataFim" name="dataFim" value="${dataFim}" formatString="dd/MM/yyyy"/></label>

	<label><span>Tipo Arquivo</span><g:select name="tipoArquivo" from="${TipoArquivo.values()}" optionValue="name" noSelection="[null:'']"></g:select></label>
	<div class="clear">
		<button class="show" type="button" onClick="filtrarArquivos();">Pesquisar</button>	
	</div>
</fieldset>

<gui:dataTable 
			id="arqSearchDT"
			controller="arquivo" action="listAllJSON"
			columnDefs="[
				[key:'id',hidden:true],
				[key:'date',sortable:true,resizeable:true,label:'Data/Hora'],
				[key:'tipo',sortable:true,resizeable:true,label:'Tipo'],
				[key:'status',sortable:true,resizeable:true,label:'Status'],
				[key:'nome',sortable:true,resizeable:true,label:'Nome Arquivo'],
				[key:'acao',sortable:true,resizeable:true,label:'Ação']
			]"
			sortedBy="id"
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
	
	function filtrarArquivos(){

		tipoArquivo=$("select[name='tipoArquivo']").val();
		dataInicio=$("#dataInicio").val();
		dataFim=$("#dataFim").val();
		
		var params="tipoArquivo="+tipoArquivo+"&dataInicio="+dataInicio+"&dataFim="+dataFim; 

		filtrarEntidade(GRAILSUI.arqSearchDT,params)
	
	}
	
</script> 			
	


