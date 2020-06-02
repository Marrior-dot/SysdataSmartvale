package chart
/**
 * Created by andrecunha on 19/10/15.
 */
class ChartModel {
    transient static String[] fillColor = ["rgba(88,206,88,0)", "rgba(234, 67, 53,0)", "rgba(67, 134, 245,0)", "rgba(251, 188, 5,0)", "rgba(67, 67, 67,0)"]
    transient static String[] strokeColor = ["rgba(88,206,88,1)", "rgba(234, 67, 53,1)", "rgba(67, 134, 245,1)", "rgba(251, 188, 5,1)", "rgba(67, 67, 67,1)"]
    transient static String[] pointColor = ["rgba(88,206,88,1)", "rgba(234, 67, 53,1)", "rgba(67, 134, 245,1)", "rgba(251, 188, 5,1)", "rgba(67, 67, 67,1)"]
    transient static String[] pointStrokeColor = ["#fff", "#fff", "#fff", "#fff", "#fff"]
    transient static String[] pointHighlightFill = ["#fff", "#fff", "#fff", "#fff", "#fff"]
    transient static String[] pointHighlightStroke = ["rgba(88,206,88,1)", "rgba(234, 67, 53,1)","rgba(67, 134, 245,1)","rgba(251, 188, 5,1)","rgba(67, 67, 67,1)"]
    
    List labels = []
    List<DataSet> datasets = []

    static def organizeMissingLabels(transactions, mes){
        def listaLabels = []
        def labelsAusentes = []

        def dias = 31
        if(mes == 2)
            dias = 29
        else if(mes == 4 || mes == 6 || mes == 9 || mes == 11)
            dias = 30

        dias.times(){ numero ->
            numero++
            transactions.each {
                listaLabels << it[1]
            }

            if(!listaLabels.contains(numero)){
                labelsAusentes << numero
            }
        }


        labelsAusentes.each { label ->
            transactions << [0, label]
        }
        transactions.sort{it[1]}
    }
    
    def addDataSet(DataSet dataset) {
        def index = datasets.size()
        dataset.fillColor = fillColor[index]
        dataset.strokeColor = strokeColor[index]
        dataset.pointColor = pointColor[index]
        dataset.pointStrokeColor = pointStrokeColor[index]
        dataset.pointHighlightFill = pointHighlightFill[index]
        dataset.pointHighlightStroke = pointHighlightStroke[index]
        datasets << dataset
    }
}

