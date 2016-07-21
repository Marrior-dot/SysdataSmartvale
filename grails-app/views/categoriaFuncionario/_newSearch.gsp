<script>
    $(function () {
        $('#buttonAdd').click(function () {
            document.getElementById('bodyTable').innerHTML += '<tr>'+
                    '<td><input type="enable" name="nome" required="required" class="enable form-control" ></td>'+
                    '<td><input type="enable" name="valor" required="required" class="enable form-control" placeholder="R$"></td>'+
                    '<td>'+
                    '<button class="btn btn-primary" name="salvarButton">Salvar</button>'+
                    '<button class="btn btn-default" name="cancelarButton">Cancelar</button>'+
                    '</td>'+
                    '</tr>'
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
        <table id="categoriaTable">
            <thead>
            <th>Nome</th>
            <th>Valor da Carga</th>
            <th>Ações Disponíveis</th>
            </thead>
            <tbody id="bodyTable">
                <tr>
                    <td>Motorista</td>
                    <td>R$ 30,00</td>
                    <td>
                        <button class="btn btn-primary">Editar</button>
                        <button class="btn btn-danger">Excluir</button>
                    </td>
                </tr>

                <tr>
                    <td><input type="enable" name="nome" required="required" class="enable form-control" ></td>
                    <td><input type="enable" name="valor" required="required" class="enable form-control" placeholder="R$"></td>
                    <td>
                        <button class="btn btn-primary" name="salvarButton">Salvar</button>
                        <button class="btn btn-default" name="cancelarButton">Cancelar</button>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
