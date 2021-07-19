import com.sysdata.gestaofrota.*

def cobSrv=ctx.getBean('cicloCobrancaService')

/*
2º Teste: Referencia no mês seguinte

Esperado:
Cartão criado - cobrar Emissão Cartão
Funcionarios ativos - cobrar Mensalidade
Nenhuma transação - não cobrar Utilização
Nenhum cartão reemitido - não cobrar reemissão
*/

def cicAb=CicloCobranca.cicloAberto
println "Ref: $cicAb.referencia"


def dtRef=new Date()
dtRef.set(date:1,month:7)

cobSrv.execute(dtRef)


def txUtlLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.TAXA_UTILIZACAO)}
def mensLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.MENSALIDADE)}
def emsCarLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.EMISSAO_CARTAO)}
def remCarLanc=Lancamento.withCriteria{eq("tipo",TipoLancamento.REEMISSAO_CARTAO)}

def funcTotal=Funcionario.count()

assert txUtlLanc.size()==0
assert mensLanc.size()==funcTotal
assert emsCarLanc.size()==funcTotal
assert remCarLanc.size()==0

println "OK"