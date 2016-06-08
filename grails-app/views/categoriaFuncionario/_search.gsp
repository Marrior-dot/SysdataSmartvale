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
			<span class="button">
				<g:link controller="categoriaFuncionario" action="create" class="btn btn-default">
					<span class="glyphicon glyphicon-plus"></span> Criar Categoria de Funcionario
				</g:link>
			</span>
		</div>
		<br><br>
		%{--<fieldset class="search">
			<input type="hidden" id="categId" name="categId"/>
			<input type="hidden" name="rhId" value="${rhId}"/>
			
			<input type="radio" class="enable" name="opcaoCat" value="1" checked="true">Código</input>
			<input type="radio" class="enable" name="opcaoCat" value="2">Nome</input>
			<br><br>
			<label>Filtro: <input class="form-control" type="search" name="filtroCateg" value="${filtro}"/></label>
		</fieldset>--}%


	<g:if test="${rhInstance.categoriasFuncionario}">
		<table class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
			<thead>
				<th>Nome</th>
				<th>Valor</th>
			</thead>
			<tbody>
				<g:each in="${rhInstance.categoriasFuncionario}" var="categoria" status="i">
					<tr>
						<td>${categoria.nome}</td>
						<td><g:formatNumber number="${categoria.valorCarga}" type="currency" format="#.##0,00" currencySymbol="R\$ "></g:formatNumber></td>
					</tr>
				</g:each>
			</tbody>
		</table>
	</g:if>
		
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






