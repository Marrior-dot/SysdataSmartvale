<%--
  Created by IntelliJ IDEA.
  User: hyago
  Date: 29/09/17
  Time: 11:24
--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="bootstrap-layout"/>
    <title>Selecioar Rh e Unidade</title>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.4/css/select2.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.4/js/select2.min.js"></script>

    <script type="application/javascript">
        $(document).ready(function () {
            var rhSelect = $("select#rh\\.id");
            var unidadeSelect = $("select#unidade\\.id");

            rhSelect.select2();
            unidadeSelect.select2({
                placeholder: 'Selecione uma Unidade apos escolher um RH',
                ajax: {
                    url: unidadeSelect.data("url"),
                    delay: 250,
                    dataType: 'json',
                    data: function (params) {
                        var query = {
                            'nome': params.term,
                            'rh.id': rhSelect.val()
                        };
                        return query;
                    },
                    processResults: function (data) {
                        var unidades = [];
                        data.forEach(function (unidade) {
                            unidades.push({id: unidade.id, text: unidade.nome});
                        });

                        return {results: unidades};
                    }
                }
            });
        });
    </script>
</head>

<body>
<br/>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4>Selecioar Rh e Unidade</h4>
    </div>

    <div class="panel-body">
        <g:if test="${flash.error}">
            <div class="alert alert-danger" role="alert">${flash.error}</div>
        </g:if>

        <g:form controller="equipamento" action="create">
            <div class="row">
                <div class="form-group col-md-4">
                    <label for="rh.id">Selecionae o Rh:</label>
                    <g:select name="rh.id" class="form-control" from="${rhInstanceList}"
                              optionKey="id" optionValue="nome" required=""/>
                </div>

                <div class="form-group col-md-6">
                    <label for="unidade.id">Selecionae a Unidade:</label>
                    <g:select name="unidade.id" class="form-control" from="[]" required=""
                              data-url="${createLink(controller: 'unidade', action: 'find.json')}"/>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">
                <i class="glyphicon glyphicon-send"></i>
                Enviar
            </button>
        </g:form>
    </div>
</div>

</body>
</html>