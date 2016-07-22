<script>
    $(function () {
        carregarTabela();

        function salvarCategoria(nome, valor) {
            $.ajax({
                url: "${g.createLink(controller:'categoriaFuncionario', action:'salvarCategoria')}",
                data: {prgId:$("#rhId").val(), nome: nome, valorCarga:valor},
                dataType: 'json',
                success: function (data) {

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
            var valor = $(this).parent().parent().find('[name="valor"]').val();
            $(this).parent().parent().html('<td><div class="catNome">'+nome+'</div></td>'+
                                                    '<td><div class="catValor">'+valor+'</div></td>'+
                                                    '<td>'+
                                                        '<button class="btn btn-primary editarButton">Editar</button> '+
                                                        '<button class="btn btn-danger excluirButton">Excluir</button>'+
                                                    '</td>');
            salvarCategoria(nome, valor);
        });

        $('#tabelaCategoria').on('click','tr .editarButton', function () {
            var nome = $(this).parent().parent().find('[class="catNome"]').text();
            var valor = $(this).parent().parent().find('[class="catValor"]').text();
            $(this).parent().parent().html('<td><input type="enable" name="nome" required="required" class="enable form-control" value="'+nome+'" ></td>'+
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
