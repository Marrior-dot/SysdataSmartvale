package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.TestesService

class BaseFuncionariosController {

    //DemonstrativoDesempenhoService demonstrativoDesempenhoService

    BaseFuncionariosService  baseFuncionariosService

    //TestesService testesService

    def index() {


        //testesService.list()

        if (params.f && params.f != 'html') {

        }

        [baseFuncionariosList: baseFuncionariosService.list(params), baseFuncionariosCount: baseFuncionariosService.count()]

    }
}
