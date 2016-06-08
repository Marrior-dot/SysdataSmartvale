<script type="text/javascript">

	$(document).ready(function(){
		$("#btnSaveEst").click(function(){

			$.ajax({
				type:'POST',
				url:"${createLink(action:'saveEstabs')}",
				data:"prgId="+$("input[name='prgId']}").val(),

				success:function(o){
					if(o.type=="error")
						showError(o.message);
					else
						showMessage("Empresas salvas")
				},
				statusCode:{
					404:function(){
							showError("Falhar ao tentar conexão ao servidor");
						}
				}
			})
			
		})
	})

</script>


<g:hiddenField name="prgId" value="${rhInstance?.id}"/>

<div class="buttons">
	<span class="button">
		<input type="button" id="btnSaveEst" class="btn btn-default" value="Salvar"/>
	</span>
	
</div>
<br><br>

<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
	<thead>
	<th><input type="checkbox" class="enable" id="geral"></th>
	<th>Razão Social</th>
	<th>Nome Fantasia</th>
	<th>CNPJ</th>
	</thead>
	<tbody>
	<g:each in="${com.sysdata.gestaofrota.PostoCombustivel.list()}" status="i" var="posto">
		<tr>
			<td><input type="checkbox" class="enable"></td>
			<td>${posto.nome}</td>
			<td>${posto.nomeFantasia}</td>
			<td>${posto.cnpj}</td>
		</tr>
	</g:each>
	</tbody>
</table>

<fr:checkDataTable
	id="estabDT"
	controller="rh" action="listEmpresasJSON"
	params="[prgId:rhInstance?.id]"
	columnDefs="[
					[key:'selecao',label:'Seleção',formatter:'checkbox'],
	 				[key:'id',hidden:true],
	 				[key:'fantasia',sortable:true,resizeable:true,label:'Nome Fantasia'],
	 				[key:'razao',sortable:true,resizeable:true,label:'Razão Social'],
            	]"
	sortedBy="fantasia"
	rowsPerPage="10"
	paginatorConfig="[
	nextPageLinkLabel:'Prox',
	previousPageLinkLabel:'Ant',
	firstPageLinkLabel:'Prim',
	lastPageLinkLabel:'Ult',
	template:'{FirstPageLink} {PreviousPageLink}  {PageLinks} {NextPageLink} {LastPageLink} {CurrentPageReport}',
	pageReportTemplate:'{totalRecords} total de registros']"
	
	txtSelAll="Todas Empresas"
	keyCheck="selecao"

/>


