<script>
    $(function () {
        carregarTabela();

        function excluirCategoria(categoriaId) {
            waitingDialog.show('Aguarde...');
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'excluirCategoria')}",
                data: {prgId:$("#rhId").val(), categoriaId: categoriaId},
                dataType: 'json',
                success: function (data) {
                    console.log(data.retorno)
                    document.getElementById("mensagem").innerHTML =  "<div class='alert alert-info'>Excluido com Sucesso</div>";
                },
                complete: function () {
                    waitingDialog.hide();
                }
            })
        }

        function salvarCategoria(nome, valor, categoriaId, component) {
            waitingDialog.show('Aguarde...');
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'salvarCategoria')}",
                data: {prgId:$("#rhId").val(), categoriaNome: nome, categoriaValor:valor, categoriaId: categoriaId},
                dataType: 'json',
                success: function (data) {
                    document.getElementById("mensagem").innerHTML =  "<div class='alert alert-info'>"+data.mensagem+"</div>";
                    $(component).parent().parent().html(         '<input type="hidden" name="categoriaId" value="'+data.id+'">'+
                            '<td><div class="catNome">'+nome+'</div></td>'+
                            '<td><div class="catValor">'+valor.replace(".", ",")+'</div></td>'+
                            '<td>'+
                            '<button class="btn btn-primary editarButton">Editar</button> '+
                            '<button class="btn btn-danger excluirButton">Excluir</button>'+
                            '</td>');
                },
                complete: function () {
                    waitingDialog.hide();
                }
            });
        }

        function carregarTabela() {
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'carregarCategorias')}",
                data: {prgId:$("#rhId").val()},
                dataType: 'html',
                success: function (data) {
                    document.getElementById('tabelaCategoria').innerHTML = data;
                }
            })
        }


        $('#buttonAdd').click(function () {
            document.getElementById('bodyTable').innerHTML += '<tr>'+
                    '<td><input type="text" name="nome" required="required" class="enable form-control" ></td>'+
                    '<td><input type="text" name="valor" required="required" class="enable form-control money"></td>'+
                    '<td>'+
                    '<button class="btn btn-primary salvarButton" name="salvarButton">Salvar</button> '+
                    '<button class="btn btn-default cancelarButton" name="cancelarButton">Cancelar</button>'+
                    '</td>'+
                    '</tr>';

            configInputMasks();
        });

        $('#tabelaCategoria').on('click','tr .excluirButton', function () {
            var confirmacao = confirm('Você tem certeza?');
            if(confirmacao){
                var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
                excluirCategoria(categoriaId);
                $(this).parent().parent().remove();
            }
        })

        $('#tabelaCategoria').on('click','tr .cancelarButton', function () {
            var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
            var componente = $(this).parent().parent();
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'cancelarCategoria')}",
                data: {categoriaId: categoriaId},
                dataType: 'json',
                success: function (data) {
                    componente.html('<input type="hidden" name="categoriaId" value="'+categoriaId+'">'+
                            '<td><div class="catNome">'+data.nome+'</div></td>'+
                            '<td><div class="catValor">'+data.valorCarga.replace(".", ",")+'</div></td>'+
                            '<td>'+
                            '<button class="btn btn-primary editarButton">Editar</button> '+
                            '<button class="btn btn-danger excluirButton">Excluir</button>'+
                            '</td>');

                }
            });
        });

        $('#tabelaCategoria').on('click','tr .salvarButton', function () {
            var nome = $(this).parent().parent().find('[name="nome"]').val();
            var valor = "R$ "+$(this).parent().parent().find('[name="valor"]').val();
            var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
            salvarCategoria(nome, valor, categoriaId, $(this));
        });

        $('#tabelaCategoria').on('click','tr .editarButton', function () {
            var nome = $(this).parent().parent().find('[class="catNome"]').text();
            var valor = $(this).parent().parent().find('[class="catValor"]').text().replace("R$ ", "");
            var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
            $(this).parent().parent().html('<input type="hidden" name="categoriaId" value="'+categoriaId+'">'+
                    '<td><input type="text" name="nome" required="required" class="enable form-control" value="'+nome+'" ></td>'+
                    '<td><input type="text" name="valor" required="required" class="enable form-control money" placeholder="R$" value="'+valor.replace(".", ",")+'" ></td>'+
                    '<td>'+
                    '<button class="btn btn-primary salvarButton" name="salvarButton">Salvar</button> '+
                    '<button class="btn btn-default cancelarButton" name="cancelarButton">Cancelar</button>'+
                    '</td>');
        });
    });
</script>
<br><br>
<div id="mensagem">

</div>
<button class="btn btn-default" id="buttonAdd"> Adicionar Categoria</button>
<br><br>
<div class="panel panel-default">
    <div class="panel-heading">
        Categorias
    </div>
    <div class="panel-body">
        <div id="tabelaCategoria">

        </div>
    </div>
</div>
