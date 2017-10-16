/**
 * Created by hyago on 10/10/17.
 */
// Para utilizar essa função, é necessário declarar um select no seu html contendo
// uma lista de MarcaVeiculo ou TipoEquipamento (de acrodo com a marca da sua instancia).
// Set sua visibility: hidden para não aparecer ao usuário
function updateNomeEmbossing(idPrimeiroCampo, idSegundCampo) {
    var programaMaquina = $("input[type='hidden']#programa-maquina").val();
    if (programaMaquina == undefined || programaMaquina == 'false' || !programaMaquina) return;

    var tamMaxPrimeiroCampo = 8;
    var tamMaxEmbossing = $("input#tam-max-embossing").val();
    var momeEmbossing = $("input#complementoEmbossing");
    var primeiroCampo = $("input#" + idPrimeiroCampo).val();
    var idMarca = $("select#" + idSegundCampo.replace('.', '\\.') + " option:selected").val();
    var marcasAbrevComp = $("select#marcas\\.abreviacao");

    if (marcasAbrevComp.length == 0) {
        console.error("Abreviação das marcas não encontrada.");
        return;
    }

    var marca = marcasAbrevComp.find('option[value="' + idMarca + '"]').text();
    primeiroCampo = primeiroCampo.length > 0 ? primeiroCampo : idPrimeiroCampo.split('.')[0];
    marca = marca.length > 0 ? marca : idSegundCampo.split('.')[0];

    var addonText = primeiroCampo.substring(0, tamMaxPrimeiroCampo).toUpperCase() + " " + marca.toUpperCase() + " ";
    var modeloMaxLength = tamMaxEmbossing - addonText.length;

    momeEmbossing.attr('maxlength', modeloMaxLength);
    momeEmbossing.val(momeEmbossing.val().substring(0, modeloMaxLength));
    $("strong#tam-max-embossing-str").text(modeloMaxLength);
    $("span#placa-modelo-addon").text(addonText);
}