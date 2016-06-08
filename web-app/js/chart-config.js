/**
 * Created by andrecunha on 19/10/15.
 */

$(function () {
    function getNumeroMes(mes){
        switch(mes) {
            case 'Jan':return 1;
                break;
            case 'Fev':return 2;
                break;
            case 'Mar':return 3;
                break;
            case 'Abr':return 4;
                break;
            case 'Mai':return 5;
                break;
            case 'Jun':return 6;
                break;
            case 'Jul':return 7;
                break;
            case 'Ago':return 8;
                break;
            case 'Set':return 9;
                break;
            case 'Out':return 10;
                break;
            case 'Nov':return 11;
                break;
            case 'Dez':return 12;
                break;
            default: return 'Todos';
        }
    }
    function changeAno() {
        var ano = $('#anoEscolhido').val();
        var mes = getNumeroMes($('#mesEscolhido').val());
        var action = "dataGraficoResgate";
        if($('#mesEscolhido').val()=='Todos'){
            action = "dataGraficoResgate";
        } else {
            action = "dataGraficoMesResgate";
        }


        $.ajax('home/'+ action, {
            data: { ano: ano, mes:mes },
            success: function(data) {
                $("#line-chart-transacoes").html("<canvas style=\"width: 100%\"></canvas>");
                var ctx = $("#line-chart-transacoes canvas").get(0).getContext("2d");
                new Chart(ctx).Line(data, {});
            }
        });
    }
    changeAno();

    $('#anoEscolhido').change(changeAno);
    $('#mesEscolhido').change(changeAno);
    //Chart.defaults.global.responsive = true;

});
