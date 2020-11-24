package com.sysdata.commons

import com.sysdata.gestaofrota.User
import grails.gorm.transactions.Transactional

@Transactional
class AccessKeyService {

    def forgetPassword() {

    }


    def changePassword(User user, String password, String confirmation) {
        def ret = [:]

        if (password == confirmation) {
            user.password = password
            user.save(flush: true)
            ret.success = true
            ret.message = "Senha alterada com sucesso"
        } else {
            ret.success = false
            ret.message = "Confirmação não confere com senha"
        }

        return ret
    }
}
