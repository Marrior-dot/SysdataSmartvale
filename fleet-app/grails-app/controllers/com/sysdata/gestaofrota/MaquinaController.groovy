package com.sysdata.gestaofrota

class MaquinaController {

    MaquinaService maquinaService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "dateCreated"
        params.order = "desc"
        [
            maquinaList: maquinaService.list(params),
            maquinaCount: maquinaService.count(params)
        ]
    }

}
