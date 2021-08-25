<%@ page import="com.sysdata.gestaofrota.TipoCartao; com.sysdata.gestaofrota.StatusCartao" %>
<g:form controller="${controller}">

    <div class="panel panel-default">
        <div class="panel-heading">Pesquisa</div>

        <div class="panel-body">
            <g:render template="/components/rhUnidadeSelect"></g:render>

            <div class="row">
                <div class="col-md-3">
                    <label class="control-label" for="numero">Número</label>
                    <g:textField name="numero" class="form-control" value="${params.numero}"></g:textField>
                </div>
                <div class="col-md-3">
                    <label class="control-label" for="status">Status</label>
                    <g:select name="status" class="form-control"
                                from="${StatusCartao.values()}" optionValue="nome"
                                noSelection="['': '-- Todos --']"
                                value="${params.status}"></g:select>
                </div>
                <div class="col-md-3">
                    <label class="control-label" for="tipo">Tipo</label>
                    <g:select name="tipo" class="form-control"
                                from="${TipoCartao.values()}" optionValue="name"
                                noSelection="['': '-- Todos --']"
                                value="${params.tipo}"></g:select>
                </div>
            </div>
        </div>
        </div>

        <div class="panel-footer">
            <button type="submit" class="btn btn-default"><span class="glyphicon glyphicon-search"></span>&nbsp;Pesquisar</button>
        </div>
    </div>


    <div class="list panel-top">
        <table id="cardsTable" class="table table-striped table-bordered table-hover table-condensed table-default">
            <thead>
            <th>Nº</th>
            <th>Portador</th>
            <th>Data Validade</th>
            <th>Status</th>
            <th>Tipo</th>
            </thead>

            <tbody>
            <g:each in="${cartaoList}" var="crt">
                <tr>
                    <td><g:link action="show" id="${crt.id}">${crt.numeroMascarado}</g:link></td>
                    <td>${crt.portador.nomeEmbossing}</td>
                    <td><g:formatDate date="${crt.validade}" format="dd/MM/yyyy"></g:formatDate></td>
                    <td>${crt.status.nome}</td>
                    <td>${crt.tipo?.name}</td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <g:paginate total="${cartaoCount}" params="${params}"></g:paginate>
    </div>
</g:form>

<script type="text/javascript">

    $(document).ready(function () {

/*
        $("#cardsTable").DataTable({
            //"serverSide": true,
            "ajax": {
                "url": "${createLink(action:'listAllJSON')}",
                "dataSrc": "results"
            },
            "columns": [
                {"data": "numero"},
                {"data": "portador"},
                {"data": "validade"},
                {"data": "status"}
            ]
        });
*/



    });
</script>



