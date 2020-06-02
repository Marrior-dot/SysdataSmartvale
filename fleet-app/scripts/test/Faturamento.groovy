import com.sysdata.gestaofrota.StatusCorte
import com.sysdata.gestaofrota.StatusFatura

def dataCorte=new Date().clearTime()
def cal=dataCorte.toCalendar()
cal.set(Calendar.DAY_OF_MONTH,14)
cal.set(Calendar.MONTH,2)
cal.set(Calendar.YEAR,2018)
dataCorte=cal.time


Rh rh=Rh.get(3)
Corte corteAtual=rh.corteAberto

def f=ctx.getBean('faturamentoService')
f.executar(dataCorte)

//Corte
assert corteAtual.dataFechamento==dataCorte
assert corteAtual.status==StatusCorte.FECHADO

//Faturas
Conta contaPort=Conta.get(6)
Fatura fatPort=conta.ultimaFatura

assert fatPort.status==StatusFatura.ABERTA
assert fatPort.valorTotal==12.98







