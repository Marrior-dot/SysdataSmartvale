security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	active = true
	cacheUsers=false

	loginUserDomainClass = "com.sysdata.gestaofrota.User"
	authorityDomainClass = "com.sysdata.gestaofrota.Role"
	requestMapClass = "com.sysdata.gestaofrota.Requestmap"
	
	useRequestMapDomainClass=false
	
	requestMapString = """\
CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
PATTERN_TYPE_APACHE_ANT
/login/auth=IS_AUTHENTICATED_ANONYMOUSLY
/js/**=IS_AUTHENTICATED_ANONYMOUSLY
/css/**=IS_AUTHENTICATED_ANONYMOUSLY
/images/**=IS_AUTHENTICATED_ANONYMOUSLY
/plugins/**=IS_AUTHENTICATED_ANONYMOUSLY
/=IS_AUTHENTICATED_FULLY
/**=ROLE_ADMIN,ROLE_PROC
"""
	
}
