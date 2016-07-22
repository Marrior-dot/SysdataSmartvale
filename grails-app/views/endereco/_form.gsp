<%@ page import="com.sysdata.gestaofrota.Estado" %>
<script type="application/javascript">

    %{--$(document).ready(function () {--}%
        %{--if($("input[type=hidden]#action").val() === 'editando'){--}%
            %{--filtrarCidadesPorEstado('${endereco}');--}%
        %{--}--}%
    %{--});--}%

    function procurarEndereco(endereco){
        var fields = {
            cep: $("input[name='" + endereco + "\\.cep']"),
            logradouro: $("input[name='" + endereco + "\\.logradouro']"),
            complemento: $("input[name='" + endereco + "\\.complemento']"),
            bairro: $("input[name='" + endereco + "\\.bairro']"),
        };

        var cep = fields.cep.val().replace(/\D/g, '');
        //caso o cep esteja errado ou logradouro ja tenha sido preenchido
        if(cep.length != 8 || fields.logradouro.val().length > 0) return;

        waitingDialog.show();

        $.getJSON("//viacep.com.br/ws/" + cep + "/json/?callback=?", function (dados) {
            if (!("erro" in dados)) {
                fields.logradouro.val(dados.logradouro);
                fields.complemento.val(dados.complemento);
                fields.bairro.val(dados.bairro);

                var estado = dados.uf;
                var cidade = dados.localidade;

                filtrarCidadesPorEstado(endereco, estado, cidade);
            }
            else {
                waitingDialog.hide();
            }
        });
    }

    function filtrarCidadesPorEstado(endereco, estadoSelecionado, cidadeSelecionada){
        var output = {
            estado: typeof estadoSelecionado !== 'undefined' ? estadoSelecionado : $("select#" + endereco + "\\.cidade\\.estado\\.id").val(),
            cidade: cidadeSelecionada
        };

        waitingDialog.show();
        $.ajax({
            url: "${g.createLink(controller:'endereco', action:'filtrarCidadesPorEstado')}",
            data: output,
            dataType: 'json',

            success: function (data) {
                var cidadesSelectBox = $("select#" + endereco + "\\.cidade\\.id");
                cidadesSelectBox.empty();
                $.each(data.cidadesDisponiveis, function(i, cidade){
                    cidadesSelectBox.append($('<option>').text(cidade.nome).attr('value', cidade.id));
                });

                //se for para selecionar um estado
                if(typeof estadoSelecionado !== 'undefined'){
                    $("select#" + endereco + "\\.cidade\\.estado\\.id option[value='" + data.estadoSelecionado.id + "']").attr('selected','selected');
                }

                //se for para selecionar uma cidade apos o carregamento da lista de estados
                if(typeof cidadeSelecionada !== 'undefined'){
                    $("select#" + endereco + "\\.cidade\\.id option[value='" + data.cidadeSelecionado.id + "']").attr('selected','selected');
                }
            },
            complete: function(){
                waitingDialog.hide();
            }
        });
    }

    function limparEndereco(endereco) {
        $("input#" + endereco + "\\.cep").val("");
        $("input#" + endereco + "\\.logradouro").val("");
        $("input#" + endereco + "\\.numero").val("");
        $("input#" + endereco + "\\.complemento").val("");
        $("input#" + endereco + "\\.bairro").val("");
        $("select#" + endereco + "\\.cidade\\.id").val("");
        $("select#" + endereco + "\\.cidade\\.estado\\.id").val("");
    }
</script>

<div class="panel panel-default">

	<div class="panel-heading">${legend}</div>
	<div class="panel-body">

		<div class="row">
            <div class="col-xs-2">
                <label class="control-label" for="endereco.cep">
                    <g:message code="endereco.cep.label" default="CEP"/>
                </label>
                <input type="text" class="form-control cep" name="${endereco}.cep" value="${enderecoInstance?.cep}"
                       onblur="procurarEndereco('${endereco}')" required/>
            </div>

            <div class="col-xs-8">
                <bs:formField id="${endereco}.logradouro" name="${endereco}.logradouro" label="Logradouro"  value="${enderecoInstance?.logradouro}" />
            </div>
            <div class="col-xs-2">
                <bs:formField id="${endereco}.numero" name="${endereco}.numero" label="Nº"  value="${enderecoInstance?.numero}" />
            </div>
		</div>

        <div class="row">
            <div class="col-xs-12">
                <bs:formField id="${endereco}.complemento" name="${endereco}.complemento" label="Complemento"  value="${enderecoInstance?.complemento}" />
            </div>
        </div>

        <div class="row">
			<div class="col-xs-4">
				<bs:formField id="${endereco}.bairro" name="${endereco}.bairro" label="Bairro"  value="${enderecoInstance?.bairro}" />
			</div>

            <div class="col-xs-4">
                <label class="control-label" for="endereco.estado.id">
                    <g:message code="endereco.estado.label" default="Estado"/>
                </label>
                <g:select class="form-control " name="${endereco}.cidade.estado.id" from="${Estado.list(sort: 'nome')}"
                          optionKey="id" optionValue="nome" required="" value="${enderecoInstance?.cidade?.estado.id}"
                onchange="filtrarCidadesPorEstado('${endereco}')"/>
            </div>

            <div class="col-xs-4">
                <label class="control-label" for="endereco.cidade.id">
                    <g:message code="endereco.cidade.label" default="Cidade"/>
                </label>
                <g:select class="form-control " name="${endereco}.cidade.id" from="${enderecoInstance?.cidade?.estado?.cidades}"
                          optionKey="id" optionValue="nome" required="" value="${enderecoInstance?.cidade.id}" />
            </div>

        </div>

	</div>
</div>