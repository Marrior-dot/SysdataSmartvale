import com.sysdata.gestaofrota.*

def cobSrv=ctx.getBean('cicloCobrancaService')

//1º Teste: Referencia no mesmo mês 
//Esperado: Nenhum lançamento
def dtRef=new Date()

cobSrv.execute(dtRef)

def txUtlLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.TAXA_UTILIZACAO)}
def mensLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.MENSALIDADE)}
def emsCarLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.EMISSAO_CARTAO)}
def remCarLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.REEMISSAO_CARTAO)}


assert txUtlLanc.size()==0
assert mensLanc.size()==0
assert emsCarLanc.size()==0
assert remCarLanc.size()==0

println "OK"