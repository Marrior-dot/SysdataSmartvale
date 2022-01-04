package com.sysdata.commons

import com.sysdata.commons.notification.SenderMailService
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserService
import com.sysdata.gestaofrota.exception.BusinessException
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class ForgetPasswordTokenService {

    SenderMailService senderMailService
    GrailsApplication grailsApplication

    def requestNewPassword(String email, String emailConfirm) {
        User user = User.findWhere(email: email)
        if (user) {
            if (email == emailConfirm) {
                ForgetPasswordToken createdToken = ForgetPasswordToken.findWhere(user: user,
                                                                                    statusToken: StatusToken.CREATED)
                if (createdToken) {
                    createdToken.statusToken = StatusToken.INVALID
                    createdToken.save(flush: true)
                }
                ForgetPasswordToken newToken = new ForgetPasswordToken(user: user,
                                                                        key: UUID.randomUUID().toString(),
                                                                        expirationDatetime: new Date() + 1)
                newToken.save(flush: true)
                // Envia email
                senderMailService.sendMessage('forget.password', [
                                                                    user: user,
                                                                    token: newToken.key,
                                                                    grailsApplication: grailsApplication
                                                                ])
            } else
                throw new BusinessException("Confirmação não confere com email informado!")
        } else
            throw new BusinessException("Não existe usuário com o email informado!")
    }

    def useToken(String token) {
        ForgetPasswordToken foundToken = ForgetPasswordToken.findWhere(key: token)
        if (foundToken) {
            def now = new Date()
            if (now <= foundToken.expirationDatetime) {
                if (foundToken.statusToken == StatusToken.CREATED) {
                    foundToken.statusToken = StatusToken.USED
                    foundToken.save(flush: true)
                }
            }
            else {
                foundToken.statusToken = StatusToken.EXPIRED
                foundToken.save(flush: true)
            }
            return foundToken
        } else
            return null

    }
}
