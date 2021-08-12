


CorteDataRepasse corteDataRepasse = new CorteDataRepasse()
def dataRepasse = new Date()
dataRepasse.set([dayOfMonth: 19, month: 6, year: 2021])
corteDataRepasse.execute(ctx.getBean('corteReembolsoEstabsService'), dataRepasse.clearTime())

class CorteDataRepasse {
    def execute(corteEstabService, dataRepasse) {
        corteEstabService.execute(dataRepasse)
    }
}