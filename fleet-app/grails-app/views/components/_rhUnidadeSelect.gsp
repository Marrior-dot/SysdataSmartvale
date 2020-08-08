<%@ page import="com.sysdata.gestaofrota.Unidade; com.sysdata.gestaofrota.Rh" %>
<div class="row">
    <div class="col-md-3">
        <label class="control-label" for="empresa">Empresa</label>
        <g:select name="empresa"
                  from="${Rh.ativos.list()}" class="form-control"
                  optionKey="id"
                  optionValue="nomeFantasia" noSelection="['': '--Selecione uma Empresa--']"
                  value="${params.empresa}"
            />
    </div>
    <div class="col-md-3">
        <label class="control-label" for="unidade">Unidade</label>
        <g:select name="unidade" from="${Unidade.findAllByRh(Rh.get(params.empresa))}" class="form-control"
                  optionKey="id" optionValue="nome"
                  value="${params.unidade}"
                  noSelection="['': '--Selecione uma Unidade--']"/>
    </div>
</div>



<script>

    $("select[name=empresa]").change(function() {
        carregarUnidades($(this).val());

        $(this).trigger("selectEmpresa");
    })


    function carregarUnidades(rhId) {

        $.getJSON("${createLink(controller: 'unidade', action: 'getAllByRh')}", { rhId: rhId },
                function(data, status) {
                    let selUnid = $("select[name=unidade]")
                    selUnid.html("")
                    if (status === 'success') {
                        let unidList = []
                        unidList.push("<option value>--Selecione uma Unidade--</option>")
                        $.each(data, function(i, unid) {
                            unidList.push("<option value='" + unid.id + "'>" + unid.name + "</option>")
                        })
                        selUnid.append(unidList)
                    } else {
                        alert("Erro no Servidor (Código: " + status + ")")
                    }
                })
    }


</script>