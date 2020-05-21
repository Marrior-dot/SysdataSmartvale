<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoIntervalo" %>
<%@ page import="com.sysdata.gestaofrota.ReembolsoSemanal" %>
<%@ page import="com.sysdata.gestaofrota.TipoReembolso" %>

<script type="text/javascript" src="${resource(dir:'js',file:'plugins/bootbox/bootbox.min.js') }"></script>

<style>
    .modal-dialog {
        z-index: 1500;
    }

</style>


<br/>
<div class="row">
	<div class="col-xs-12">
		<label for="tipoReembolso">Tipo Reembolso</label>
		<g:radioGroup class="enable" name="tipoReembolso"
					  labels="${TipoReembolso.values()*.nome}"
					  values="${TipoReembolso.values()}"
					  value="${postoCombustivelInstance?.tipoReembolso}">
			<p>${it.radio} ${it.label}</p>
		</g:radioGroup>
	</div>
</div>

<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
	<button type="button" class="btn btn-default" onclick="openModal(0);">
		<i class="glyphicon glyphicon-plus"></i>&nbsp;Adicionar Reembolso
	</button>
</sec:ifAnyGranted>



<div id="divSemanal" class="panel-top">

    <div class="list">
        <table id="rbSemanalTable" class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
                <th>Dia Semana</th>
                <th>Intervalo Dias</th>
                <th>Açoes</th>
            </thead>
        </table>
    </div>


</div>

<sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
	<div id="divIntervalo" >


        <div class="list">
            <table id="rbIntervaloTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                <thead>
                <th>Inicio Intervalo</th>
                <th>Fim Intervalo</th>
                <th>Dia Efetivaçao</th>
                <th>Meses</th>
                <th>Açoes</th>
                </thead>
            </table>
        </div>



	</div>
</sec:ifAnyGranted>



<script type="text/javascript">

    var rbSemanalTable,rbIntervaloTable

    $(document).ready(function(){

        rbSemanalTable=$("#rbSemanalTable").DataTable({
            "ajax":{
                "url":"${createLink(controller:'postoCombustivel',action:'getReembolsoSemanal')}",
                "data":{"id":${postoCombustivelInstance?.id}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"diaSemana"},
                {"data":"intervaloDias"},
                {"data":"acao"}
            ]
        });


        rbIntervaloTable=$("#rbIntervaloTable").DataTable({
            "ajax":{
                "url":"${createLink(controller:'postoCombustivel',action:'getIntervalosReembolso')}",
                "data":{"id":${postoCombustivelInstance?.id}},
                "dataSrc":"results"
            },
            "columns":[
                {"data":"inicio"},
                {"data":"fim"},
                {"data":"diaEfetivacao"},
                {"data":"meses"},
                {"data":"acao"}
            ]
        });
    });


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
						alert(o.message);

						if(checked.val()=='SEMANAL')
                            rbSemanalTable.ajax.reload();
						else if(checked.val()=='INTERVALOS_MULTIPLOS')
                            rbIntervaloTable.ajax.reload();

					}
					else if(o.type=="error")
						alert(o.message);
				},
				statusCode:{
					404:function(){
						alert("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});	
		}		
	}

    function openReembSemanal(html){
        bootbox.dialog({
            title: "Reembolso Semanal",
            message:html,
            buttons: {
                success: {
                    label: "Salvar",
                    className: "btn-success",
                    callback: function() {
                        saveReembSemanal();
                    }
                }
            }
        });
    }

    function openReembIntervalo(html){
        bootbox.dialog({
            title: "Intervalos Multiplos",
            message:html,
            buttons: {
                success: {
                    label: "Salvar",
                    className: "btn-success",
                    callback: function() {
                        saveReembIntervalos();
                    }
                }
            }
        });
    }



    function loadReembSemanal(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel',action:'manageReembolsoSemanal')}",
            data:"parId=${postoCombustivelInstance?.id}&id="+rbId,
            success: function (data) {
                openReembSemanal(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });

    }

    function loadReembIntervalo(rbId) {
        $.ajax({
            type: 'POST',
            url: "${createLink(controller:'postoCombustivel',action:'manageReembolso')}",
            data:"parId=${postoCombustivelInstance?.id}&id="+rbId,
            success: function (data) {
                openReembIntervalo(data);
            },
            statusCode: {
                404: function () {
                    openMessage('error', "Falha ao abrir página para inclusão de Novos Funcionários");
                }
            }
        });

    }


	function openModal(rbId){

		if(checked!=null){

			if(checked.val()=='SEMANAL')
                loadReembSemanal(rbId)
			else if(checked.val()=='INTERVALOS_MULTIPLOS')
                loadReembIntervalo(rbId)

		}else{
			alert("Selecione primeiramente um Tipo de Reembolso!");
		}
	}

	function saveReembIntervalos(){
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
                        rbIntervaloTable.ajax.reload();
					}
					else if(o.type=="error")
						alert(o.message);
				},
				statusCode:{
					404:function(){
						alert("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});						
		}else{
			showErrorList();
		}
	}

	function saveReembSemanal(){
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
						alert(o.message);
                        rbSemanalTable.ajax.reload();

					}
					else if(o.type=="error")
						alert(o.message);
				},
				statusCode:{
					404:function(){
						alert("Falha ao abrir página para inclusão de Novos Funcionários");
					}
				}
			});			
		}else{
			showErrorList();
		}
	}
	
          
</script>