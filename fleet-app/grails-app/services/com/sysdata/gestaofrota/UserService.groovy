package com.sysdata.gestaofrota

import grails.gorm.transactions.Transactional

@Transactional
class UserService {

    def grailsApplication

    def register(command) {
		def user = new User([username: command.username, email: command.email, password: command.password,
			accountLocked: false, enabled: true])
		if(!user.save(failOnError: true, flush: true)){
			
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

}
