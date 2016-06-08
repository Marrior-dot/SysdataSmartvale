	    
<script type="text/javascript">
	$(function () {
		openWindow();
	});

	function openWindow(){
		$.ajax({
			type:'POST',
			url:"${createLink(controller:'funcionario',action:'search')}",
			data:"gestor=true&controller=funcionario&unidade_id=${equipamentoInstance?.unidade?.id}&action=filtro",
			success:function(data){
				$("#searchForm").html(data);
				GRAILSUI.searchFuncDialog.show();
			},
			statusCode:{
				404:function(){
					openMessage('error',"Falha ao abrir página para inclusão de Novos Funcionários");
				}
			}
		});
	}

	function removeFuncionario(idFunc){
		$.ajax({
			type:'POST',
			url:"${createLink(controller:'equipamento',action:'removeFuncionario')}",
			data:"idEquip=${equipamentoInstance?.id}"+"&idFunc="+idFunc,
			beforeSend:function(data){
				$("#message").html("Removendo relação entre Funcionário e Equipamento...");
				$("#message").show();
			},
			success:function(data){
				//Recarrega a lista de funcionários relacionados ao veículo
				updateDataTable();
				$("#message").html(data);
				$("#message").show();
				$("#message").fadeOut(5000);
			}
			
		});
	}

	function updateDataTable(){

		var params="id=${equipamentoInstance?.id}"; 

		filtrarEntidade(GRAILSUI.funcionariosDT,params); 
	}

	var yesHandler=function(o) {

		var selected=GRAILSUI.funcSearchDT.getRecord(GRAILSUI.funcSearchDT.getSelectedRows()[0]);
		selFuncId=selected.getData('id')
		
		var exists=false;
        if(selFuncId){
            var set=GRAILSUI.funcionariosDT.getRecordSet();
            var rec
            for(i=0;i<set.getLength();i++){
                rec=set.getRecord(i);
                if(rec.getData('id')==selFuncId){
                    exists=true;
                    break;
                }
            }
			if(exists){
				showError("Nova relação inválida! Funcionário já relacionado com equipamento");
			}else{
				$.ajax({
					type:'POST',
					url:"${createLink(controller:'equipamento',action:'addFuncionario')}",
					data:"idEquip=${equipamentoInstance?.id}"+"&idFunc="+selFuncId,
					beforeSend:function(data){
						$("#message").html("Incluindo relação entre Funcionário e Equipamento...");
						$("#message").show();
					},
					success:function(data){
						//Recarrega a lista de funcionários relacionados ao equipamento
						updateDataTable();
						$("#message").html(data);
						$("#message").show();
						$("#message").fadeOut(5000);
					},
					error:function(err){
						showError("Erro ao inserir relação!");	
					}
				});
			}
        }else{
			showMessage("Nenhum funcionário foi selecionado!");
        }
        this.cancel();
    }
	
</script>

		<h2>Funcionários habilitados para condução</h2>
		<br>
		    	<gui:dataTable 
		    			id="funcionariosDT"
		    			controller="equipamento" action="listFuncionariosJSON"
		    			columnDefs="[
		    				[key:'id',hidden:true],
		    				[key:'matricula',sortable:true,resizeable:true,label:'Matrícula'],
		    				[key:'nome',sortable:true,resizeable:true,label:'Nome'],
		    				[key:'cpf',sortable:true,resizeable:true,label:'CPF'],
		    				[key:'acao',label:'Ação']
		    			]"
		    			sortedBy="nome"
		    			rowsPerPage="10"
		    			params="[id:equipamentoInstance?.id]"
		    			paginatorConfig="[
		    				nextPageLinkLabel:'Prox',
		  previousPageLinkLabel:'Ant',
		  firstPageLinkLabel:'Prim',
		  lastPageLinkLabel:'Ult',
		    				template:'{FirstPageLink} {PreviousPageLink}  {PageLinks} {NextPageLink} {LastPageLink} {CurrentPageReport}',
		    				pageReportTemplate:'{totalRecords} total de registros'
		    			]"
		    			/> 
	
		<span id="message"></span>

			<gui:dialog
				id="searchFuncDialog"
				title="Pesquisa de Funcionários"
				draggable="false"
				width="500px"
				update="searchForm"
				fixedcenter="true"
				close="false"
				modal="true"
				buttons="[
				        [text:'Selecionar', handler: 'yesHandler', isDefault: true],
				        [text:'Cancelar', handler: 'function() {this.cancel();}', isDefault: false]
				    ]"
				>
				<div id="searchForm"></div>			
			</gui:dialog>
			
			
			 <div class="buttons">
			 	<span class="button"><input class="btn btn-default" type="button" class="new" onclick="openWindow();" value="Adicionar Funcionário"/></span>
			 </div>
			

