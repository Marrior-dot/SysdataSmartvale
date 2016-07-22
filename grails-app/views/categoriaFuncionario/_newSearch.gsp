<script>
    $(function () {
        carregarTabela();

        function excluirCategoria(categoriaId) {
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'excluirCategoria')}",
                data: {prgId:$("#rhId").val(), categoriaId: categoriaId},
                dataType: 'json',
                success: function (data) {

                }
            })
        }

        function salvarCategoria(nome, valor, categoriaId) {
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'salvarCategoria')}",
                data: {prgId:$("#rhId").val(), categoriaNome: nome, categoriaValor:valor, categoriaId: categoriaId},
                dataType: 'json',
                success: function (data) {
                    console.log(data);
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
                    '<td><input type="enable" name="nome" required="required" class="enable form-control" ></td>'+
                    '<td><input type="enable" name="valor" required="required" class="enable form-control" placeholder="R$"></td>'+
                    '<td>'+
                    '<button class="btn btn-primary salvarButton" name="salvarButton">Salvar</button> '+
                    '<button class="btn btn-default cancelarButton" name="cancelarButton">Cancelar</button>'+
                    '</td>'+
                    '</tr>'
        });

        $('#tabelaCategoria').on('click','tr .excluirButton', function () {
            console.log($(this).parent().parent());
            $(this).parent().parent().remove();
        })

        $('#tabelaCategoria').on('click','tr .cancelarButton', function () {

        });

        $('#tabelaCategoria').on('click','tr .salvarButton', function () {
            var nome = $(this).parent().parent().find('[name="nome"]').val();
            var valor = "R$ "+$(this).parent().parent().find('[name="valor"]').val();
            var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
            $(this).parent().parent().html(         '<input type="hidden" name="categoriaId" value="'+categoriaId+'">'+
                                                    '<td><div class="catNome">'+nome+'</div></td>'+
                                                    '<td><div class="catValor">'+valor+'</div></td>'+
                                                    '<td>'+
                                                        '<button class="btn btn-primary editarButton">Editar</button> '+
                                                        '<button class="btn btn-danger excluirButton">Excluir</button>'+
                                                    '</td>');
            salvarCategoria(nome, valor, categoriaId);
        });

        $('#tabelaCategoria').on('click','tr .editarButton', function () {
            var nome = $(this).parent().parent().find('[class="catNome"]').text();
            var valor = $(this).parent().parent().find('[class="catValor"]').text().replace("R$ ", "");
            var categoriaId = $(this).parent().parent().find('[name="categoriaId"]').val();
            $(this).parent().parent().html('<input type="hidden" name="categoriaId" value="'+categoriaId+'">'+
                    '<td><input type="enable" name="nome" required="required" class="enable form-control" value="'+nome+'" ></td>'+
                    '<td><input type="enable" name="valor" required="required" class="enable form-control" placeholder="R$" value="'+valor+'" ></td>'+
                    '<td>'+
                    '<button class="btn btn-primary salvarButton" name="salvarButton">Salvar</button> '+
                    '<button class="btn btn-default cancelarButton" name="cancelarButton">Cancelar</button>'+
                    '</td>');
        });
    });
</script>
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
