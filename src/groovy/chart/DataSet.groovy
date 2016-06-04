package chart

import java.math.RoundingMode

/**
 * Created by mikael on 22/10/15.
 */
class DataSet {
    transient ChartModel chartModel
    String label = ""
    String fillColor = "rgba(151,187,205,0.2)"
    String strokeColor = "rgba(151,187,205,1)"
    String pointColor = "rgba(151,187,205,1)"
    String pointStrokeColor = "#fff"
    String pointHighlightFill = "#fff"
    String pointHighlightStroke = "rgba(151,187,205,1)"
    List data = []

    public DataSet(ChartModel chartModel) {
        this.chartModel = chartModel
    }

    def addData(Object label, Object data) {
        if (!chartModel?.labels?.contains(label))
            chartModel.labels << label
        this.data << new BigDecimal(data.toString()).setScale(2, RoundingMode.UP)

    }
}
