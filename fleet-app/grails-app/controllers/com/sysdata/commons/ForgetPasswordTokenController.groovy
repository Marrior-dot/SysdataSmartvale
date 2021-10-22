package com.sysdata.commons

import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserService
import com.sysdata.gestaofrota.exception.BusinessException

class ForgetPasswordTokenController {

    ForgetPasswordTokenService forgetPasswordTokenService
    UserService userService

    def index() {}

    def requestNewPassword() {
        try {
            forgetPasswordTokenService.requestNewPassword(params.email)
        } catch (BusinessException e) {
            log.error e.message
            flash.error = e.message
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
        }
        render view: 'index'
    }

    def useToken() {
        ForgetPasswordToken forgetPswToken = forgetPasswordTokenService.useToken(params.key)
        if (forgetPswToken) {
            if (forgetPswToken.statusToken == StatusToken.USED) {
                render view: 'newPassword'
                return
            } else {
                log.error "TOKEN_FORGET_PSW #${forgetPswToken.id} - ${forgetPswToken.statusToken.userMessage}"
                flash.error = forgetPswToken.statusToken.userMessage
                render view: 'userMessages'
            }
        } else {
            log.error "TOKEN_FORGET_PSW '${params.key}' não encontrado!"
            flash.error = "Token não encontrado!"
            render view: 'userMessages'
        }
    }

    def saveNewPassword() {
        try {
            User user = User.get(params.id)
            userService.saveNewPassword(user, params.newPassword, params.confirmPassword)
            flash.message = "Senha alterada com sucesso"
        } catch (BusinessException e) {
            log.error e.message
            flash.error = e.message
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
        }
        render view: 'userMessages'
    }
}
