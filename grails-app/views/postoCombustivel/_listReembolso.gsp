<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoIntervalo" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoSemanal" %>
<%@ page import="com.sysdata.gestaofrota.TipoReembolso" %>

<br/>
<div class="row">
	<div class="col-xs-12">
		<label for="tipoReembolso">Tipo Reembolso</label>
		<g:radioGroup name="tipoReembolso"
					  labels="${TipoReembolso.values()*.nome}"
					  values="${TipoReembolso.values()}"
					  value="${postoCombustivelInstance?.tipoReembolso}">
			<p>${it.radio} ${it.label}</p>
		</g:radioGroup>
	</div>
</div>

<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
	<button type="button" class="btn btn-default" onclick="openWindow(0);">
		Adicionar Reembolso
	</button>
</sec:ifAnyGranted>


<div id="divSemanal" >
	<gui:dataTable id="reembSemanalDT"
				controller="postoCombustivel" action="getReembolsoSemanal"
				columnDefs="[
					[key:'diaSemana',sortable:true,resizeable:true,label:'Dia Semana'],
					[key:'intervaloDias',sortable:true,resizeable:true,label:'Intervalo Dias'],
					[key:'acao',sortable:false,resizeable:true,label:'Ações']
				]"
				params="[id:postoCombustivelInstance?.id]"
				sortedBy="diaSemana"
				rowsPerPage="10"
				paginatorConfig="[
					nextPageLinkLabel:'Prox',
				previousPageLinkLabel:'Ant',
				firstPageLinkLabel:'Prim',
				lastPageLinkLabel:'Ult',
					template:'{FirstPageLink} {PreviousPageLink}  {PageLinks} {NextPageLink} {LastPageLink} {CurrentPageReport}',
					pageReportTemplate:'{totalRecords} total de registros'
				]"/>
	
</div>

<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
	<div id="divIntervalo" >
	
		<gui:dataTable 
					id="reembolsosDT"
					controller="postoCombustivel" action="getIntervalosReembolso"
					columnDefs="[
						[key:'inicio',sortable:true,resizeable:true,label:'Início Intervalo'],
						[key:'fim',sortable:true,resizeable:true,label:'Fim Intervalo'],
						[key:'diaEfetivacao',sortable:true,resizeable:true,label:'Dia Efetivação'],
						[key:'meses',sortable:true,resizeable:true,label:'Meses'],
						[key:'acao',sortable:false,resizeable:true,label:'Ações']
					]"
					params="[id:postoCombustivelInstance?.id]"
					sortedBy="inicio"
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
	</div>
</sec:ifAnyGranted>

<sec:ifAnyGranted roles="ROLE_ESTAB">
	<div id="divIntervalo" >
	
		<gui:dataTable 
					id="reembolsosDT"
					controller="postoCombustivel" action="getIntervalosReembolso"
					columnDefs="[
						[key:'inicio',sortable:true,resizeable:true,label:'Início Intervalo'],
						[key:'fim',sortable:true,resizeable:true,label:'Fim Intervalo'],
						[key:'diaEfetivacao',sortable:true,resizeable:true,label:'Dia Efetivação'],
						[key:'meses',sortable:true,resizeable:true,label:'Meses']
					]"
					params="[id:postoCombustivelInstance?.id]"
					sortedBy="inicio"
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
	</div>
</sec:ifAnyGranted>
			
<gui:dialog 
	id="reembolsoDialog"
	title="Reembolso em Intervalos"
	draggable="true"
	fixedcenter="true"
	close="false"
	modal="true"
    buttons="[[text:'Salvar', handler: 'saveHandler', isDefault: true], 
	    		[text:'Cancelar', handler: 'function() {this.cancel();}', isDefault: false]	]" >
	<div id="reembolsoDiv">
	
	</div>
</gui:dialog>


<gui:dialog 
	id="reembolsoSemanalDialog"
	title="Reembolso Semanal"
	draggable="true"
	fixedcenter="true"
	close="false"
	modal="true"
    buttons="[[text:'Salvar', handler: 'saveSemanalHandler', isDefault: true], 
	    		[text:'Cancelar', handler: 'function() {this.cancel();}', isDefault: false]	]" >
	<div id="reembolsoSemanalDiv">
	
	</div>
</gui:dialog>




<script type="text/javascript">

	var checked=null;

	$(function(){

		checked=$("input[name='tipoReembolso']:checked");

		<g:if test="${postoCombustivelInstance?.tipoReembolso==TipoReembolso.INTERVALOS_MULTIPLOS}">
			$("#divIntervalo").show();
			$("#divSemanal").hide();
		</g:if>

		<g:elseif test="${postoCombustivelInstance?.tipoReembolso==TipoReembolso.SEMANAL}">
			$("#divIntervalo").hide();
			$("#divSemanal").show();
		</g:elseif>

		<g:else>
			$("#divIntervalo").hide();
			$("#divSemanal").hide();
			checked=null;
		</g:else>


		$("input[name='tipoReembolso']").change(function(){
			checked=$("input[name='tipoReembolso']:checked");
			if(checked.val()=='INTERVALOS_MULTIPLOS'){
				$("#divIntervalo").show();
				$("#divSemanal").hide();
			}else if(checked.val()=='SEMANAL'){
				$("#divIntervalo").hide();
				$("#divSemanal").show();
			}
		})
			
		
	});


	function deleteReembolso(rbId){
		var del=confirm('Confirma a exclusão do Reembolso?');
		if(del){
			$.ajax({
				type:'POST',
				url:"${createLink(controller:'postoCombustivel',action:'deleteReembolso') }",
				data:"id="+rbId,
				success:function(o){
					if(o.type=="ok"){
						showMessage(o.message);

						var params="id=${postoCombustivelInstance?.id}";
						if(checked.val()=='SEMANAL'){

							filtrarEntidade(GRAILSUI.reembSemanalDT,params); 
							GRAILSUI.reembolsoSemanalDialog.hide();
							
						}else if(checked.val()=='INTERVALOS_MULTIPLOS'){

							filtrarEntidade(GRAILSUI.reembolsosDT,params)
							GRAILSUI.reembolsoDialog.hide();
						}
					}
					else if(o.type=="error")
						showError(o.message);
				},
				statusCode:{
					404:function(){
						showError("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});	
		}		
	}

	function openWindow(rbId){
		var callback = {  
			success: function(o) {  

				if(checked.val()=='SEMANAL'){
					GRAILSUI.util.replaceWithServerResponse(document.getElementById('reembolsoSemanalDiv'), o);  
					GRAILSUI.reembolsoSemanalDialog.show();
					
				}else if(checked.val()=='INTERVALOS_MULTIPLOS'){
					GRAILSUI.util.replaceWithServerResponse(document.getElementById('reembolsoDiv'), o);  
					GRAILSUI.reembolsoDialog.show();
				}

			},  
			failure:function(o) {}  
		};  

		if(checked!=null){

			if(checked.val()=='SEMANAL'){
				YAHOO.util.Connect.asyncRequest('POST',"${createLink(controller:'postoCombustivel',action:'manageReembolsoSemanal')}?parId=${postoCombustivelInstance?.id}&id="+rbId, callback);
			}else if(checked.val()=='INTERVALOS_MULTIPLOS'){
				YAHOO.util.Connect.asyncRequest('POST',"${createLink(controller:'postoCombustivel',action:'manageReembolso')}?parId=${postoCombustivelInstance?.id}&id="+rbId, callback);
			}
			
		}else{
			alert("Selecione primeiramente um Tipo de Reembolso!");
		}
	}

	function saveHandler(){
		hasError=false;
		$(".mandatory").each(function(){
			if($(this).val()==""){
				addErrorList("Campo ["+$(this).parent().get(0).childNodes[0].innerHTML+"] obrigatório");
				hasError=true;
			}
		});		
		if(!hasError){
			var data=$("#reembolsoForm").serialize();
			$.ajax({
				type:'POST',
				url:"${createLink(controller:'postoCombustivel',action:'saveReembolso')}",
				data:data,
				success:function(o){
					if(o.type=="ok"){
						showMessage(o.message);

						var params="id=${postoCombustivelInstance?.id}";
						filtrarEntidade(GRAILSUI.reembolsosDT,params); 
						GRAILSUI.reembolsoDialog.hide();
					}
					else if(o.type=="error")
						showError(o.message);
				},
				statusCode:{
					404:function(){
						showError("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});						
		}else{
			showErrorList();
		}
	}

	function saveSemanalHandler(){
		hasError=false;
		$(".required").each(function(){
			if($(this).val()==""){
				addErrorList("Campo ["+$(this).parent().get(0).childNodes[0].innerHTML+"] obrigatório");
				hasError=true;
			}
		});		

		if(!hasError){
			var data=$("#reembolsoSemanalForm").serialize();
			$.ajax({
				type:'POST',
				url:"${createLink(controller:'postoCombustivel',action:'saveReembolsoSemanal')}",
				data:data,
				success:function(o){
					if(o.type=="ok"){
						showMessage(o.message);

						var params="id=${postoCombustivelInstance?.id}";
						filtrarEntidade(GRAILSUI.reembSemanalDT,params); 
						GRAILSUI.reembolsoSemanalDialog.hide();
					}
					else if(o.type=="error")
						showError(o.message);
				},
				statusCode:{
					404:function(){
						showError("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});			
		}else{
			showErrorList();
		}
	}
	
          
</script>