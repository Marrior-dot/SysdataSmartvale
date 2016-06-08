<fieldset>
    <input type="hidden" id="selectedId" name="selectedId"/>
    <input type="radio" name="opcao" value="1" checked="true">Matrícula</input>
    <input type="radio" name="opcao" value="2">Nome</input>
    <input type="radio" name="opcao" value="3">CPF</input>
    <br><br>
    <label>Filtro: <g:textField class="form-control" name="filtro" value="${filtro}"/></label>
</fieldset>


<input type="checkbox" name="selectAll" checked>Seleciona Todos Funcionários</input>

<gui:dataTable
        id="funcSearchDT"
        controller="pedidoCarga" action="${(action=='novo')?'listFuncionariosCategoriaJSON':'listFuncionariosPedidoJSON'}"
        params="${(action=='novo')?[unidade_id:unidadeInstance?.id]:[id:pedidoCargaInstance?.id]}"
        columnDefs="[
                [key:'selecao',label:'Seleção',formatter:'checkbox'],
                [key:'id',hidden:true],
                [key:'categoria',hidden:true],
                [key:'matricula',sortable:true,resizeable:true,label:'Matrícula'],
                [key:'nome',sortable:true,resizeable:true,label:'Nome'],
                [key:'cpf',sortable:true,resizeable:true,label:'CPF'],
                [key:'valorCarga',label:'Valor Carga', formatter:'text', editor:[controller:'pedidoCarga', action:'updateValorCarga']]
            ]"

        sortedBy="nome"
        rowsPerPage="100"
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

	/*
		Faz o controle da quantidade de funcionarios selecionados 
		e a compara com a quantidade total para marcar ou desmarcar a seleção de todos (checkbox)
	*/
	function validateSelectAll(catId,check){

		var count=countCat[catId];
		var total=totalCat[catId];

		count=check?(count+1):(count-1);

		countCat[catId]=count;

		$("input[name='selectAll']").attr("checked",(total==count));
	}

	function synchServer(funcId,check){

		$.ajax({
			type:'POST',
			url:"${createLink(controller:'pedidoCarga',action:'synchServer') }",
			data:"funcId="+funcId+"&check="+check,
			success:function(o){
				if(o.type=="error")
					showError(o.message);
			},
			statusCode:{
				404:function(){
					showError("Falha ao abrir página para inclusão de Novos Funcionários");
				}
			}
		});	
	}


	function synchCheckAll(categ,check){
		$.ajax({
			type:'POST',
			url:"${createLink(controller:'pedidoCarga',action:'synchCheckAll')}",
			data:((categ!=null)?'categ='+categ:"categ=all&unidId=${unidadeInstance?.id}")+"&check="+check,
			success:function(o){
				if(o.type=="error")
					showError(o.message);
			},
			statusCode:{
				404:function(){
					showError("Falha ao abrir página para inclusão de Novos Funcionários");
				}
			}
		})
	}


	YAHOO.util.Event.onDOMReady(function(){

		var firstTime=true;

		/* No carregamento da tabela de funcionários por categoria:
		 * 	1. Registra o total de funcionários por categoria;
		 *	2. Verifica o checkbox de seleção de todos por categoria
		 *	Obs: (workaround) Na primeira vez que este evento é capturado a seleção da primeira categoria ainda não foi realizada, 
		 * 	por isso o tratamento com a variável firstTime 
		 */		
		 
		GRAILSUI.funcSearchDT.subscribe("initEvent",function(args){

			if(!firstTime){
				var rec=GRAILSUI.funcSearchDT.getRecordSet().getRecords()[0];
				
				if(totalCat[rec.getData('categoria')]==undefined)
					totalCat[rec.getData('categoria')]=GRAILSUI.funcSearchDT.configs.paginator._configs.totalRecords.value;	
				
				if(countCat[rec.getData('categoria')]==undefined)
					countCat[rec.getData('categoria')]=GRAILSUI.funcSearchDT.configs.paginator._configs.totalRecords.value;

				$("input[name='selectAll']").attr("checked",(countCat[rec.getData('categoria')]==totalCat[rec.getData('categoria')]));
					
			}else
				firstTime=false;
		});


        GRAILSUI.funcSearchDT.subscribe("dataReturnEvent",function(args){

            var check=$("input[name='selectAll']").checked?true:false;

            var recs=GRAILSUI.funcSearchDT.getRecordSet().getRecords();
            var i=0;

            while(i<recs.length){

                //if(recs[i]!=undefined && recs[i].getData('selecao')!=check)
                    recs[i].setData('selecao',check);
                i++;
            }

            GRAILSUI.funcSearchDT.render();

        });


		GRAILSUI.funcSearchDT.subscribe("rowSelectEvent",function(args){
			var selected=GRAILSUI.funcSearchDT.getRecord(GRAILSUI.funcSearchDT.getSelectedRows()[0]);
			$("#selectedId").val(selected.getData('id'));
		});

		/*
		 *	Pega REGISTRO e COLUNA do DT a partir do html element (checkbox)
		 *	Seta o valor do registro agora com o que foi efetivamente informado
		 *	Desmarca o check de Seleçao de Todos
		 *	
		 */
		
		GRAILSUI.funcSearchDT.subscribe("checkboxClickEvent",function(args){
			
			var elCheck=args.target;
			var newValue=elCheck.checked;
			var rec=this.getRecord(elCheck);
			var col=this.getColumn(elCheck)
			
			rec.setData(col.key,newValue);

			synchServer(rec.getData('id'),newValue);

<%--			if(newValue)--%>
<%--				totalPedido+=parseFloat(rec.getData('valorCarga'));--%>

			validateSelectAll(rec.getData('categoria'),newValue);
			
		});
	});
	
	//Filtra enquanto digita
	$('input[name="filtro"]').keyup(function(){
		filtrarFuncionarios(null,$(this).val());
	});


	var funcSel=[];

	/* 
	 * Quando cheka a seleção de Todos
	 * Envia notificação para marcação de todos por categoria
	 */

	$("input[name='selectAll']").click(function(){

		var check=$(this).is(":checked");

		var recs=GRAILSUI.funcSearchDT.getRecordSet().getRecords();
		var i=0;


		while(i<recs.length){

			//if(recs[i]!=undefined && recs[i].getData('selecao')!=check)
				recs[i].setData('selecao',check);
			i++;
		}
		GRAILSUI.funcSearchDT.render();

		var catSel=GRAILSUI.categoriasDT.getRecord(GRAILSUI.categoriasDT.getSelectedRows()[0]);
		if(catSel)
			synchCheckAll(catSel.getData('id'),check)
		else
			synchCheckAll(null,check);

		/* Redefine contador de funcionários selecionados para o total de funcionários por categoria */
		countCat[catSel.getData('id')]=totalCat[catSel.getData('id')];
		

	});

	

	function filtrarFuncionarios(categId,filtro){
		var params='';

		<g:if test="${unidadeInstance}">
			params+='unidade_id='+${unidadeInstance?.id}
		</g:if>
		
		<g:if test="${pedidoCargaInstance}">
			params+='&id='+${pedidoCargaInstance?.id}
		</g:if>

		if(categId)
			params+='&categId='+categId;
		if(filtro)
			params+="&opcao="+$(':checked').val()+"&filtro="+filtro;

		filtrarEntidade(GRAILSUI.funcSearchDT,params);
	
	}
</script>	

