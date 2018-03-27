def dataCorte=new Date().clearTime()
def cal=dataCorte.toCalendar()
cal.set(Calendar.DAY_OF_MONTH,14)
cal.set(Calendar.MONTH,2)
cal.set(Calendar.YEAR,2018)
dataCorte=cal.time


def f=ctx.getBean('faturamentoService')
f.executar(dataCorte)