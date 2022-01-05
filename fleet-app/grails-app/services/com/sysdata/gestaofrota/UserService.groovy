package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.exception.BusinessException
import grails.gorm.transactions.Transactional

@Transactional
class UserService {

    def grailsApplication

    private static def PASSWORD_PATTERN = /^(?=(.*[a-z])+)(?=(.*[A-Z])+)(?=(.*[0-9])+)(?=(.*[!@#\%^&*()\\-__+.])+).{14,}$/

    def register(command) {
		def user = new User([username: command.username, email: command.email, password: command.password,
			accountLocked: false, enabled: true])
		if(!user.save(flush: true)){
			
		}
		def userRole = Role.findByAuthority('ROLE_USER')
		if(userRole){
			UserRole.create(user, userRole, true)
		}else{
			throw new RuntimeException("User Roler not found")
		}
    }

	def saveNew(User userInstance, Map params) {

		def ret = [ success: true, message: null]

		if (params.password == params.confirmPassword) {
			params.findAll { it.key.contains("ROLE_") && it.value == "on" }.each { k, v ->
				userInstance.save()
				Role role = Role.findByAuthority(k)
				UserRole.create userInstance, role
			}
			ret.message = "Usuário $userInstance.username criado"

		} else {
			ret.success = false
			ret.message = "Confirmação não confere com Senha informada"
		}
		ret
	}

    def update(User user, Map params) {
        user.properties = params
        def userRoles = UserRole.findAllByUser(user)
        userRoles.each { userRole ->
            UserRole.remove(user, userRole.role)
        }
        params.findAll { it.key.contains("ROLE_") && it.value == "on" }.each { k, v ->
            user.save(failOnError: false)
            Role role = Role.findByAuthority(k)
            UserRole.create user, role
        }
    }



    boolean validPassword(password) {
        return password ==~ PASSWORD_PATTERN
    }

    def saveNewPassword(User user, String newPassword, String confirmPassword) {
        if (validPassword(newPassword)) {
            if (newPassword == confirmPassword) {
                user.password = newPassword
                user.save(flush: true)
            } else
                 throw new BusinessException("Confirmação não corresponde à Nova Senha!")
        } else
            throw new BusinessException("Nova senha não está no padrão de senha forte!")
    }

}
