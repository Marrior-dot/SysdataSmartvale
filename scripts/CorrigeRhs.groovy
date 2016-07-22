import com.sysdata.gestaofrota.Rh
import com.sysdata.gestaofrota.Unidade

Rh.list().each { rh ->
    if (!rh.taxaEmissaoCartao)
        rh.taxaEmissaoCartao = 10
    if (!rh.maximoTrnPorDia)
        rh.maximoTrnPorDia = 10
    if (!rh.taxaReemissaoCartao)
        rh.taxaReemissaoCartao = 10
    if (!rh.taxaMensalidade)
        rh.taxaMensalidade = 10
    if (!rh.taxaUtilizacao)
        rh.taxaUtilizacao = 10
    if (!rh.diasInatividade)
        rh.diasInatividade = 10

    if(!rh.save()) println(rh.errors)
}

def rhsUnidadesZeradas = Rh.createCriteria().list {
    sizeEq("unidades", 0)
}

rhsUnidadesZeradas.each {
    def unidade = new Unidade(codigo: '0', nome: 'Unidade de Correção', dateCreated: new Date())
    if(!unidade.save(flush: true)) println("Problemas ao salvar unidade de correção:\n${it.errors}")
    it.addToUnidades(unidade)
    if(!it.save(flush: true)) println("Problemas ao salvar RH:\n${it.errors}")
}

return "Todos RHs corrigidos"
