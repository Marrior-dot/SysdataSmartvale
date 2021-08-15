package com.sysdata.gestaofrota

class FuncionarioCadastroController {

    FuncionarioCadastroService funcionarioCadastroService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "dateCreated"
        params.order = "desc"
        [
            funcionarioList: funcionarioCadastroService.list(params),
            funcionarioCount: funcionarioCadastroService.count(params)
        ]
    }

}
