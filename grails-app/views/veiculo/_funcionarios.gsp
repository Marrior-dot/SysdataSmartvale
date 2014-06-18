	    
<script type="text/javascript">

	function openWindow(){
		$.ajax({
			type:'POST',
			url:"${createLink(controller:'funcionario',action:'search')}",
			data:"controller=funcionario&unidade_id=${veiculoInstance?.unidade?.id}&action=filtro",
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
			url:"${createLink(controller:'veiculo',action:'removeFuncionario')}",
			data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+idFunc,
			beforeSend:function(data){
				$("#message").html("Removendo relação entre Funcionário e Veículo...");
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

		var params="id=${veiculoInstance?.id}"; 

		GRAILSUI.funcionariosDT.customQueryString=params;
		GRAILSUI.funcionariosDT.loadingDialog.show();
		GRAILSUI.funcionariosDT.cleanup();
		
		var sortedBy = GRAILSUI.funcionariosDT.get('sortedBy');
		var newState = {
		  startIndex: 0,
		  sorting: {
		      key: sortedBy.key,
		      dir: ((sortedBy.dir == YAHOO.widget.DataTable.CLASS_DESC) ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC)
		  },
		  pagination : {
		      recordOffset: 0,
		      rowsPerPage: GRAILSUI.funcionariosDT.get("paginator").getRowsPerPage()
		  }
		};
		
		var oCallback = {
			success: GRAILSUI.funcionariosDT.onDataReturnInitializeTable,
		  	failure: GRAILSUI.funcionariosDT.onDataReturnInitializeTable,
		  	scope: GRAILSUI.funcionariosDT,
		  	argument: newState
		};
			
		GRAILSUI.funcionariosDT.getDataSource().sendRequest(GRAILSUI.funcionariosDT.buildQueryString(newState), oCallback);

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
				showError("Nova relação inválida! Funcionário já relacionado com veículo");
			}else{
				$.ajax({
					type:'POST',
					url:"${createLink(controller:'veiculo',action:'addFuncionario')}",
					data:"idVeic=${veiculoInstance?.id}"+"&idFunc="+selFuncId,
					beforeSend:function(data){
						$("#message").html("Incluindo relação entre Funcionário e Veículo...");
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
		    			controller="veiculo" action="listFuncionariosJSON"
		    			columnDefs="[
		    				[key:'id',hidden:true],
		    				[key:'matricula',sortable:true,resizeable:true,label:'Matrícula'],
		    				[key:'nome',sortable:true,resizeable:true,label:'Nome'],
		    				[key:'cpf',sortable:true,resizeable:true,label:'CPF'],
		    				[key:'acao',label:'Ação']
		    			]"
		    			sortedBy="nome"
		    			rowsPerPage="10"
		    			params="[id:veiculoInstance?.id]"
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
			 	<span class="button"><input type="button" class="new" onclick="openWindow();" value="Adicionar Funcionário"/></span>
			 </div>
			
