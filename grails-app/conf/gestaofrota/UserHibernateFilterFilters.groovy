package gestaofrota

import com.sysdata.gestaofrota.Role

class UserHibernateFilterFilters {
	def grailsApplication
	def sessionFactory
	def springSecurityService
    def filters = {
        all(controller:'reportViewer', action:'*') {
            before = {
				def session = sessionFactory.currentSession
				def user=springSecurityService.currentUser
				def enableFilters=grailsApplication.config.hibernateFilter?.enableFilters
				if (user && enableFilters && enableFilters instanceof Closure) {
					def result=enableFilters(user)
					def roleEstab=Role.findByAuthority('ROLE_ESTAB')
					def roleRH=Role.findByAuthority('ROLE_RH')
					if(result) {
						result.each {fname, options->
							log.debug "Adding filter $fname => $options ..."
							def clz=options['class']
							def parameters=options['parameters']
							if(user.authorities.contains(roleEstab)){
								if(fname=='transacaoPorPosto' || fname=='estabelecimentoPorPosto'){
									def filter=clz.enableHibernateFilter(fname)
									parameters.each {pname,value->
										filter.setParameter(pname,value)
									}
								}
							}
							if(user.authorities.contains(roleRH)){
								if(fname=='transacaoPorRH'){
									def filter=clz.enableHibernateFilter(fname)
									parameters.each {pname,value->
										filter.setParameter(pname,value)
									}
								}
							}
						}
					}	
				}
            }
        }
    }
}
