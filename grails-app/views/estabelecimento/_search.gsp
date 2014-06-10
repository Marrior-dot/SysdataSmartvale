<%@ page import="com.sysdata.gestaofrota.Util" %>
<g:form controller="${controller}">
	
	<input type="hidden" name="empId" value="${empId}"/>
	
	<gui:dataTable 
				id="estSearchDT"
				controller="estabelecimento" action="listAllJSON"
				params="[empId:empId]"
				columnDefs="[
					[key:'id',hidden:true],
					[key:'codEstab',sortable:true,resizeable:true,label:'Cod.Estab.'],
					[key:'razao',sortable:true,resizeable:true,label:'Razão Social'],
					[key:'nomeFantasia',sortable:true,resizeable:true,label:'Nome Fantasia'],
					[key:'acao',sortable:false,resizeable:false,label:'Ação']
				]"
				sortedBy="codEstab"
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


	<g:if test="${!action || action==Util.ACTION_VIEW}">
		<div class="buttons">
			<span class="button"><g:actionSubmit class="new" action="create" value="${message(code:'default.new.label', args:[message(code:'estabelecimento.label')]) }"/></span>
		</div>
	</g:if>
	
</g:form>
