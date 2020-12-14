package com.sysdata.gestaofrota

class LoteEmbossingController {

    LoteEmbossingService loteEmbossingService

    def index() {
        def max = params.max ? params.max as int : 10
        def offset = params.offset ? params.offset as int : 0

        [
            lotesEmbossingList: loteEmbossingService.listLotesEmbossing(max, offset),
            lotesEmbossingCount: loteEmbossingService.countLotesEmbossing()
        ]
    }

    def listCartoesEmbossar() {
        def max = params.max ? params.max as int : 10
        def offset = params.offset ? params.offset as int : 0

        def list = loteEmbossingService.listCartoesParaEmbossing(max, offset)
        def count = loteEmbossingService.countCartoesParaEmbossing()

        render template: "cartoesEmbossar",
                model: [
                    cartoesEmbossarList: list,
                    cartoesEmbossarCount: count
                ]

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
