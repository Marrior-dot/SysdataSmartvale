%{--

<jq:jquery>

	function filtrarUnidades(filtro){

		var params="rhId=${rhId}&opcao="+$("input[name='opcaoUnid']:checked").val()+"&filtro="+filtro; 
	
		filtrarEntidade(GRAILSUI.unidSearchDT,params);
	
	}
	

	//Filtra enquanto digita
	$('input[name="filtro"]').keyup(function(){
		filtrarUnidades($(this).val());
	});
</jq:jquery>

--}%

<%@ page import="com.sysdata.gestaofrota.Unidade" %>


<div class="panel panel-default">
    <div class="panel-heading">Lista de RHs</div>

    <div class="panel-body">

        <g:form controller="${controller}">


            <div class="buttons">
                <g:link controller="unidade" action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span>Novo RH
                </g:link>
            </div>


        <fieldset class="search">
            <input type="hidden" id="unidId" name="unidId"/>
            <input type="hidden" name="rhId" value="${rhId}"/>

        <input type="radio" name="opcaoUnid" value="1" checked="true">Código</input>
        <input type="radio" name="opcaoUnid" value="2">Nome</input>
        <br><br>
        <label>Filtro: <g:textField name="filtro" value="${filtro}"/></label>
        </fieldset>

        <div class="list">
            <table class="table table-striped table-bordered table-hover table-condensed table-default" >
                <thead>
                <th>Código</th>
                <th>Nome</th>
                <th>Status</th>
                <th></th>
                </thead>
                <tbody>
                <g:each in="${Unidade.withCriteria{rh{eq('id',rhId)}}}" var="unidade" >
                    <tr>
                        <td><g:link controller="unidade" action="show" id="${unidade.id}"> ${unidade?.codigo}</g:link></td>
                        <td>${unidade?.nome}</td>
                        <td>${unidade?.status}</td>
                        <td></td>
                    </tr>

                </g:each>

                </tbody>
            </table>
        </div>


        </div>




    %{--

            <gui:dataTable
                        id="unidSearchDT"
                        controller="${controller}" action="listAllJSON"
                        columnDefs="[
                            [key:'id',sortable:true,resizeable:true,label:'Código'],
                            [key:'nome',sortable:true,resizeable:true,label:'Nome'],
                            [key:'status',sortable:true,resizeable:true,label:'Status'],
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
    --}%



    </g:form>

    </div>








