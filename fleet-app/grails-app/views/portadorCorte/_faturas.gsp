<div class="panel panel-default">
    <div class="panel-body">
        <div class="tabbable">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#tabFatAberta" data-toggle="tab">Fatura Aberta</a></li>
                <li><a href="#tabFatFechada" data-toggle="tab">Fatura Fechada</a></li>
                <li><a href="#tabFatAnteriores" data-toggle="tab">Faturas Anteriores</a></li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tabFatAberta"></div>
                <div class="tab-pane" id="tabFatFechada"></div>
                <div class="tab-pane" id="tabFatAnteriores"></div>
            </div>
        </div>
    </div>
</div>


<script>

    $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {

        if ($(event.target).text() === 'Faturas' || $(event.target).text() === 'Fatura Aberta') {
            findFaturaAberta("${portador.id}");

        } else if ($(event.target).text() === 'Fatura Fechada') {
            findFaturaFechada("${portador.id}");

        } else if ($(event.target).text() === 'Faturas Anteriores') {
            listFaturasAnteriores("${portador.id}");
        }

    });


    function findFaturaAberta(pid) {

        $.get("${createLink(controller: 'portadorCorte', action: 'findFaturaAberta')}",
                { prtId: pid },
                function(data) {
                    $("#tabFatAberta").html(data);
                }
        )
        .fail(function(err) {
            if (err.statusText === "error")
                alert("Erro no Servidor");
            else
                alert("Erro: " + error);

            console.log(err.statusText);
        });

    }

    function findFaturaFechada(pid) {

        $.get("${createLink(controller: 'portadorCorte', action: 'findUltimaFatura')}",
                { prtId: pid },
                function(data) {
                    $("#tabFatFechada").html(data);
                }
        )
        .fail(function(err) {
            if (err.statusText === "error")
                alert("Erro no Servidor");
            else
                alert("Erro: " + error);

            console.log(err.statusText);

        });

    }

    function listFaturasAnteriores(pid) {

        $.get("${createLink(controller: 'portadorCorte', action: 'listFaturasAnteriores')}",
                { prtId: pid },
                function(data) {
                    $("#tabFatAnteriores").html(data);
                }
        )
        .fail(function(err) {
            if (err.statusText === "error")
                alert("Erro no Servidor");
            else
                alert("Erro: " + error);

            console.log(err.statusText);
        });

    }




</script>