import com.sysdata.gestaofrota.*

def cobSrv=ctx.getBean('cicloCobrancaService')

def dtRef=new Date()

cobSrv.execute(dtRef)

