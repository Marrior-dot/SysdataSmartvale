<%@ page import="com.sysdata.gestaofrota.Util" %>
<%@ page import="com.sysdata.gestaofrota.Funcionario" %>


<div class="panel panel-default">

    <div class="panel-heading">Lista de Funcionários </div>

    <div class="panel-body">

        <g:form controller="${controller}">


                <g:if test="${!action || action==Util.ACTION_VIEW}">
                    <div class="buttons">
                        <span class="button"><g:actionSubmit class="new"
                                                             action="${unidade_id?'create':'selectRhUnidade'}"
                                                             value="${message(code:'default.new.label', args:[message(code:'funcionario.label')]) }"/></span>
                    </div>
                </g:if>


                <div class="list">
                    <table id="funcTable" class="table table-striped table-bordered table-hover table-condensed table-default">
                        <thead>
                        <th>CPF</th>
                        <th>Nome</th>
                        <th>Matrícula</th>
                        </thead>
                    </table>
                </div>



            %{--

                <fieldset class="search">
                    <h2>Pesquisa por filtro</h2>

                    <input type="hidden" name="unidade_id" value="${unidade_id}"/>

                    <input type="radio" name="opcao" value="1" checked="true">Matrícula</input>
                    <input type="radio" name="opcao" value="2">Nome</input>
                    <input type="radio" name="opcao" value="3">CPF</input>
                    <br>
                    <label>Filtro: <g:textField name="filtroFunc" value="${filtro}"/></label>
                </fieldset>

            --}%


            </g:form>


    </div>

</div>

<script type="text/javascript">

    $(document).ready(function(){

        $("#funcTable").DataTable({
            "paging":   false,
            "processing": true,
            "serverSide": true,
            "ajax":{
                "url":"${createLink(controller:'funcionario',action:'listAllJSON')}",
                "data":{"unidade_id":${unidade_id}},
				"dataSrc":"results"
            },
			"columns":[
                {"data":"cpf"},
                {"data":"nome"},
                {"data":"matricula"}
            ],
            "columnDef":[{
                "targets":0,
                "data":"cpf",
                "render":function(data,type,row,meta){
                    return "<a href='${createLink(controller:'funcionario',action:'show')}'>"+data+"</a>"
                }
            }]
		});
    });
</script>


%{--
	<gui:dataTable 
				id="funcSearchDT"
				controller="funcionario" action="listAllJSON"
				params="[unidade_id:unidade_id,gestor:gestor]"
				columnDefs="[
					[key:'id',hidden:true],
					[key:'matricula',sortable:true,resizeable:true,label:'Matrícula'],
					[key:'nome',sortable:true,resizeable:true,label:'Nome'],
					[key:'cpf',sortable:true,resizeable:true,label:'CPF'],
					[key:'acao',label:'Ação']
				]"
				sortedBy="nome"
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



<jq:jquery>

	function filtrarFuncionarios(filtro,categId){
		var params='';
		
		<g:if test="${unidade_id}">
			params+="unidade_id=${unidade_id}&";
		</g:if>
		
		if(categId)
			params+='categId='+categId+'&';
		
		params+="opcao="+$(':checked').val()+"&filtro="+filtro;
		
		filtrarEntidade(GRAILSUI.funcSearchDT,params);
	

	}

	//Filtra enquanto digita
	$('input[name="filtroFunc"]').keyup(function(){
		filtrarFuncionarios($(this).val(),null);
	});

</jq:jquery>


--}%


