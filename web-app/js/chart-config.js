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
     /*   var action = "dataGraficoResgate";
        if($('#mesEscolhido').val()=='Todos'){
            action = "dataGraficoResgate";
        } else {
            action = "dataGraficoMesResgate";
        }*/

        var data = {
            labels: ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"],
            datasets: [
                {
                    fillColor: "rgba(88,206,88,0)",
                    strokeColor: "rgba(88,206,88,1)",
                    pointColor: "rgba(88,206,88,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(88,206,88,1)",
                    data: [65, 59, 80, 81, 56, 55, 40, 65, 59, 80, 81, 56, 55, 40]
                },
                {
                    fillColor: "rgba(234, 67, 53,0)",
                    strokeColor: "rgba(234, 67, 53,1)",
                    pointColor: "rgba(234, 67, 53,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(234, 67, 53,1)",
                    data: [2, 8, 1, 3, 8, 5, 4, 5, 9, 0, 0, 2, 5, 5]
                },
                {
                    fillColor: "rgba(67, 134, 245,0)",
                    strokeColor: "rgba(67, 134, 245,1)",
                    pointColor: "rgba(67, 134, 245,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(67, 134, 245,1)",
                    data: [4, 8, 3, 1, 4, 6, 2, 6, 9, 2, 1, 0, 5, 0]
                },

                {
                    fillColor: "rgba(251, 188, 5,0)",
                    strokeColor: "rgba(251, 188, 5,1)",
                    pointColor: "rgba(251, 188, 5,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(251, 188, 5,1)",
                    data: [14, 18, 13, 11, 14, 16, 12, 16, 19, 12, 11, 10, 15, 12]
                },
                {
                    fillColor: "rgba(67, 67, 67,0)",
                    strokeColor: "rgba(67, 67, 67,1)",
                    pointColor: "rgba(67, 67, 67,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(67, 67, 67,1)",
                    data: [1, 1, 1, 1, 1, 1, 1, 6, 1, 2, 1, 1, 5, 2]
                }
            ]
        };

        $("#line-chart-transacoes").html("<canvas style=\"width: 100%\"></canvas>");
        var ctx = $("#line-chart-transacoes canvas").get(0).getContext("2d");
        new Chart(ctx).Line(data, {});

     /*   $.ajax('home/'+ action, {
            data: { ano: ano, mes:mes },
            success: function(data) {
                $("#line-chart-transacoes").html("<canvas style=\"width: 100%\"></canvas>");
                var ctx = $("#line-chart-transacoes canvas").get(0).getContext("2d");
                new Chart(ctx).Line(data, {});
            }
        });*/
        console.log("hey");
    }
    changeAno();

    $('#anoEscolhido').change(changeAno);
    $('#mesEscolhido').change(changeAno);
    //Chart.defaults.global.responsive = true;

});
