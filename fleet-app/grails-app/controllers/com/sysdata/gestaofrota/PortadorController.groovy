package com.sysdata.gestaofrota

class PortadorController {

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "id"
        params.order = "desc"
        def criteria = {
            ne("class", "com.sysdata.gestaofrota.PortadorAnonimo")
        }
        [
            portadoresList: Portador.createCriteria().list(params, criteria),
            portadoresCount: Portador.createCriteria().count(criteria)
        ]
    }

    def show(Long id) {
        Portador portador = Portador.get(id)
        if (portador.instanceOf(PortadorMaquina)) {
            PortadorMaquina portadorMaquina = portador as PortadorMaquina
            if (portadorMaquina.maquina.instanceOf(Veiculo)) {
                redirect controller: 'veiculo', action: 'show', id: portadorMaquina.maquina.id
                return
            } else if (portadorMaquina.maquina.instanceOf(Equipamento)) {
                redirect controller: 'equipamento', action: 'show', id: portadorMaquina.maquina.id
                return
            }
        } else if (portador.instanceOf(PortadorFuncionario)) {
            PortadorFuncionario portadorFuncionario = portador as PortadorFuncionario
            redirect controller: 'funcionario', action: 'show', id: portadorFuncionario.funcionario.id
            return
        }
    }
}
