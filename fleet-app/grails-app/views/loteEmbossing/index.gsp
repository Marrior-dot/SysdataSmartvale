<!DOCTYPE html>

<html>
<head>
    <meta name="layout" content="layout-restrito">

</head>
<body>
    <div class="panel panel-default panel-top">

        <div class="panel-heading">
            <h3>Lotes Embossing</h3>
        </div>
        <div class="panel-body">

            <alert:all/>

            <div class="tabbable">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#lotesEmbossing" data-toggle="tab">Lotes Embossing</a></li>
                    <li><a href="#cartoes" data-toggle="tab">Cartões a Embossar</a></li>
                </ul>

                <div class="tab-content">
                    <div class="tab-pane active" id="lotesEmbossing">
                        <g:render template="lotesEmbossing"></g:render>
                    </div>
                    <div class="tab-pane" id="cartoes">
                        <g:render template="cartoesEmbossar" ></g:render>
                    </div>
                </div>

            </div>

        </div>
    </div>


<script>

    var loadCartoesEmbossar = function(offset) {

        $.get("${createLink(action: 'listCartoesEmbossar')}", { offset: offset }, function(data) {
            $("#cartoes").html(data);
        })
    }

    $(document).ready(function() {

        $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {
            if (e.target.href.includes("#cartoes")) {
                loadCartoesEmbossar('0');
            }
        });

        // Pega evento de click nos links de paginação
        $("#cartoes").on('click', 'a.step', function() {
            event.preventDefault();

            var link = $(this).attr("href");
            var aux = link.substring(link.lastIndexOf("offset"), link.length);
            var offsetParam = aux.substring(0, aux.indexOf("&") > -1 ? aux.indexOf("&") : aux.length);
            var offset = offsetParam.split("=")[1];

            loadCartoesEmbossar(offset);
        });

    });
</script>
</body>
</html>