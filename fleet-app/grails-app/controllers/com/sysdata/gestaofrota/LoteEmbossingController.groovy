package com.sysdata.gestaofrota

class LoteEmbossingController {

    LoteEmbossingService loteEmbossingService

    def index() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0
        params.sort = "dateCreated"
        params.order = "desc"

        [
            lotesEmbossingList: loteEmbossingService.listLotesEmbossing(params),
            lotesEmbossingCount: loteEmbossingService.countLotesEmbossing()
        ]
    }

    def listCartoesEmbossar() {
        params.max = params.max ? params.max as int : 10
        params.offset = params.offset ? params.offset as int : 0

        def list = loteEmbossingService.listCartoesParaEmbossing(params)
        def count = loteEmbossingService.countCartoesParaEmbossing()

        render template: "cartoesEmbossar",
                model: [
                    cartoesEmbossarList: list,
                    cartoesEmbossarCount: count
                ],
                params: params

    }

    def createLoteEmbosing() {
        try {
            LoteEmbossing loteEmbossing = loteEmbossingService.createLoteEmbossing()
            flash.success = "Lote Embossing #${loteEmbossing.id} criado"
        } catch (e) {
            flash.error = "Erro ao criar novo Lote Embossing"
            log.error flash.error
            log.error e.message
        }
        redirect action: 'index'
    }

}
