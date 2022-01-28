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
            forgetPasswordTokenService.requestNewPassword(params.email?.trim(), params.emailConfirm?.trim())
            flash.success = "Email enviado para ${params.email}!"
            render view: 'userMessages'
            return
        } catch (BusinessException e) {
            log.error e.message
            flash.error = e.message
            render view: 'index'
            return
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
            render view: 'index'
            return
        }
    }

    def useToken() {
        ForgetPasswordToken forgetPswToken = ForgetPasswordToken.findWhere(key: params.key)
        if (forgetPswToken) {
            if (forgetPswToken.statusToken == StatusToken.CREATED) {
                forgetPasswordTokenService.useToken(forgetPswToken)
                render view: 'newPassword', model: [token: forgetPswToken]
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
        ForgetPasswordToken token
        try {
            token = ForgetPasswordToken.get(params.id as long)
            userService.saveNewPassword(token.user, params.newPassword, params.confirmPassword)
            log.info "Usuario #${token.user.username} - senha alterada por Esqueci a Senha"
            flash.success = "Senha alterada com sucesso"
            render view: 'userMessages'
            return
        } catch (BusinessException e) {
            log.error e.message
            flash.error = e.message
            render view: 'newPassword', model: [token: token]
            return
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
            render view: 'newPassword', model: [token: token]
        }
    }


}
