package com.sysdata.gestaofrota

import com.sysdata.gestaofrota.User

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

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
}