package com.sysdata.commons

import com.sysdata.commons.notification.SenderMailService
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserService
import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class ForgetPasswordTokenService {

    SenderMailService senderMailService

    def requestNewPassword(String email) {
        User user = User.findWhere(email: email)
        if (user) {
            ForgetPasswordToken createdToken = ForgetPasswordToken.findWhere(user: user, status: StatusToken.CREATED)
            if (createdToken) {
                createdToken.statusToken = StatusToken.CANCELED
                createdToken.save()
            }
            ForgetPasswordToken newToken = new ForgetPasswordToken(user: user)
            newToken.save(flush: true)

            // Envia email
            senderMailService.sendMessage('forget.password', [user: user])

        } else
            throw new BusinessException("Não existe usuário com o email informado!")
    }

    def isValidToken(String token) {
        ForgetPasswordToken foundToken = ForgetPasswordToken.findWhere(token: token)
        if (foundToken) {
            def now = new Date()
            if (now <= foundToken.expirationDatetime)
                return true
            else {
                foundToken.statusToken = StatusToken.EXPIRED
                foundToken.save()
                return false
            }
        } else
            return false
    }
}
