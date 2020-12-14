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

    var loadCartoesEmbossar = function() {

        $.get("${createLink(action: 'listCartoesEmbossar')}", function(data) {

            $("#cartoes").html(data);
        })
    }

    $(document).ready(function() {

        $('a[data-toggle="tab"]').on('show.bs.tab', function(e) {

            if (e.target.href.includes("#cartoes")) {
                loadCartoesEmbossar();
            }

        });
    });
</script>
</body>
</html>