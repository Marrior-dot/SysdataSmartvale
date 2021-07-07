<%@ page import="com.sysdata.gestaofrota.StatusLotePagamento; com.sysdata.gestaofrota.StatusEmissao" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="layout-restrito" charset="UTF-8">
    <title>Detalhes Pagamento Estabelecimento</title>
</head>
<body>
    <div class="panel panel-default panel-top">
        <div class="panel-heading">
            <h4>Detalhes Pagamento Estabelecimento</h4>
        </div>
        <div class="panel-body">
            <div class="row">
                <div class="col-md-12">
                    <g:link action="showPagamentoLote" id="${pagamentoLote.id}" class="btn btn-default"><span class="glyphicon glyphicon-arrow-left"></span> Voltar</g:link>
                </div>
            </div>
            <div class="panel panel-default panel-top">
                <div class="panel-heading">
                    <h5>Pagamento Estabelecimento #${pagamentoEstabelecimento.id}</h5>
                </div>
                <div class="panel-body">
                    <alert:all/>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Estabelecimento</label>
                            <p><g:link controller="postoCombustivel" action="show" id="${pagamentoEstabelecimento.estabelecimento.id}">${pagamentoEstabelecimento.estabelecimento.identificacaoResumida}</g:link></p>
                        </div>
                        <div class="col-md-4">
                            <label>Corte</label>
                            <p>${pagamentoEstabelecimento.corte}</p>
                        </div>
                    </div>

                    <div class="row form-group">
                        <div class="col-md-4">
                            <label>Data Prevista</label>
                            <p><g:formatDate date="${pagamentoEstabelecimento.dataProgramada}" format="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-4">
                            <label>Data Pagamento</label>
                            <p><g:formatDate date="${pagamentoEstabelecimento.dataEfetivada}" format="dd/MM/yyyy"/></p>
                        </div>
                    </div>
                </div>
            </div>

            <div id="entries"></div>

        </div>
    </div>
    <script>

        function loadEntries() {
            $.get("${createLink(action: 'loadEntries')}", {pagEstabId: "${pagamentoEstabelecimento.id}"})
                    .done(function(data) {
                        $("div#entries").html(data);
                    })
                    .fail(function() {

                    })
        }


        $(document).ready(function() {
            loadEntries();
        });
    </script>

</body>
</html>