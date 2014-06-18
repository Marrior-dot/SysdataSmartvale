package com.sysdata.gestaofrota

class User {

	transient springSecurityService

	String username
	String email
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	Participante owner
	String name 

	static constraints = {
		username blank: false, unique: true
		password blank: false
		email mail: true, blank: false, nullable: true
	}

	static mapping = {
		table 'users'
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
