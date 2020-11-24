package com.sysdata.gestaofrota.relatorios

class DemonstrativoDesempenhoRelatorioController {

    DemonstrativoDesempenhoService demonstrativoDesempenhoService

    //TestesService testesService

    def index() {


        //testesService.list()

        if (params.f && params.f != 'html') {

        }

        [desempenhoList: demonstrativoDesempenhoService.list(params), desempenhoCount: demonstrativoDesempenhoService.count()]

    }
}
