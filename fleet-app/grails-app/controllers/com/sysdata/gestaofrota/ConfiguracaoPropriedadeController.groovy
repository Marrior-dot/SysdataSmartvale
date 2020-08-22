package com.sysdata.gestaofrota

class ConfiguracaoPropriedadeController {

    def index() {
        [configList: ConfiguracaoPropriedade.list()]
    }

    def create() {
        render view: "form", model: [configuracaoPropriedade: new ConfiguracaoPropriedade(params), state: "new"]
    }

    def show(long id) {
        render view: "form", model: [configuracaoPropriedade: ConfiguracaoPropriedade.get(id), state: "view"]
    }

    def edit(long id) {
        render view: "form", model: [configuracaoPropriedade: ConfiguracaoPropriedade.get(id), state: "edit"]
    }

    def delete() {
        ConfiguracaoPropriedade configuracaoPropriedade = ConfiguracaoPropriedade.get(params.id.toLong())
        configuracaoPropriedade.delete(flush: true)
        flash.message = "Configuração ${configuracaoPropriedade.id} removida"
        redirect(action: 'index')
    }

    def save() {
        ConfiguracaoPropriedade configuracaoPropriedade = new ConfiguracaoPropriedade(params)
        if (configuracaoPropriedade.save(flush: true)) {
            flash.message = "Configuração ${configuracaoPropriedade.id} salva"
            redirect(action: "show", id: configuracaoPropriedade.id)
        }
        else
            redirect(action: "create", params: params)
    }


}
