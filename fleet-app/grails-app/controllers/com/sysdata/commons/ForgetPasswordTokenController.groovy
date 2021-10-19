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
            flash.error = e.message
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
        }
    }

    def inputNewPassword() {
        
    }

    def saveNewPassword() {
        try {
            User user = User.get(params.id)
            userService.saveNewPassword(user, params.newPassword, params.confirmPassword)
            flash.message = "Senha alterada com sucesso"
        } catch (BusinessException e) {
            flash.error = e.message
            log.error e.message
        } catch (e) {
            e.printStackTrace()
            flash.error = "Erro Interno. Contate suporte."
        }
    }
}
