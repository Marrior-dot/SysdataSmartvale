package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException
import org.apache.http.HttpStatus

class VinculoCartaoProvisorioController {

    VinculoCartaoProvisorioService vinculoCartaoProvisorioService

    def linkToCardHolder() {
        Portador cardHolder = Portador.get(params.cardHolderId as long)
        def limitDate = params.date('limitDate', 'dd/MM/yyyy')
        try {
            vinculoCartaoProvisorioService.linkToCardHolder(params.cardNumber, cardHolder, limitDate)
            log.info "Cartão provisório ${params.cardNumber} vinculado ao Portador #${params.cardHolderId}"
            render status: HttpStatus.SC_OK, text: "Cartão provisório (${params.cardNumber}) vinculado ao Portador"
        } catch (BusinessException be) {
            log.error "Erro de negócio: ${be.message}"
            render status: HttpStatus.SC_INTERNAL_SERVER_ERROR, text: be.message
        } catch (RuntimeException e) {
            e.printStackTrace()
            render status: HttpStatus.SC_INTERNAL_SERVER_ERROR, text: "Erro interno. Contatar suporte"
        }
    }

    def unlinkFromCardHolder() {
        Portador cardHolder = Portador.get(params.cardHolderId as long)
        Cartao card = Cartao.get(params.cardId.toLong())
        try {
            vinculoCartaoProvisorioService.unlinkFromCardHolder(card, cardHolder)
        } catch (BusinessException be) {
            log.error "Erro de negócio: ${be.message}"
            render status: HttpStatus.SC_INTERNAL_SERVER_ERROR, text: be.message
        } catch (RuntimeException e) {
            e.printStackTrace()
            render status: HttpStatus.SC_INTERNAL_SERVER_ERROR, text: "Erro interno. Contatar suporte"
        }
    }
}
