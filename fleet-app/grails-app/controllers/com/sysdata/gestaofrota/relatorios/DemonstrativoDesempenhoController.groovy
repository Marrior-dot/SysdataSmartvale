package com.sysdata.gestaofrota.relatorios

import com.sysdata.gestaofrota.TestesService

class DemonstrativoDesempenhoController {

    DemonstrativoDesempenhoService demonstrativoDesempenhoService

    //TestesService testesService

    def index() {


        //testesService.list()

        if (params.f && params.f != 'html') {

        }

        [desempenhoList: demonstrativoDesempenhoService.list(params), desempenhoCount: demonstrativoDesempenhoService.count()]

    }
}
