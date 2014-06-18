import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role
import com.sysdata.gestaofrota.User
import com.sysdata.gestaofrota.UserRole




class BootStrap {

    def init = { servletContext ->
		 
		

//		switch(Environment.current){
//			case Environment.DEVELOPMENT:
				createDefaultOwners()
				createDefaultUser()
				createParameters()
//				break;
//		}
    }

	def destroy = {   }
	
	void createDefaultOwners(){
		Administradora.count()==0?new Administradora(nome:"AMAZON",bin:"623409").save(failOnError:true):false
		Processadora.count()==0?new Processadora(nome:"SYSDATA").save(failOnError:true):false
	}
	
	void createDefaultUser(){
		def roleProc=Role.findByAuthority('ROLE_PROC')?:new Role(authority:'ROLE_PROC').save(failOnError:true)
		def roleAdmin=Role.findByAuthority('ROLE_ADMIN')?:new Role(authority:'ROLE_ADMIN').save(failOnError:true)
		def roleRh=Role.findByAuthority('ROLE_RH')?:new Role(authority:'ROLE_RH').save(failOnError:true)
		
		def procInstance=Processadora.findByNome("SYSDATA")
		def adminInstance=Administradora.findByNome("AMAZON")
		
		def adminUser=User.findByUsername('admin')?:new User(owner:procInstance,name:'USUARIO PROC ADMIN',username:'admin',password:'fr07@',enabled:true).save(failOnError:true)
		def luizUser=User.findByUsername('luiz.leao')?:new User(owner:procInstance,name:'LUIZ LEAO',username:'luiz.leao',password:'123',enabled:true).save(failOnError:true)
		
		if(!luizUser.authorities.contains(roleProc))
			UserRole.create luizUser,roleProc
		if(!luizUser.authorities.contains(roleAdmin))
			UserRole.create luizUser,roleAdmin
			
		if(!adminUser.authorities.contains(roleProc))
			UserRole.create adminUser,roleProc
		if(!adminUser.authorities.contains(roleAdmin))
			UserRole.create adminUser,roleAdmin
			
	}
	
	void createParameters(){
		ParametroSistema.findByNome(ParametroSistema.BIN)?:new ParametroSistema(nome:ParametroSistema.BIN,valor:"623409").save(failOnError:true)
		ParametroSistema.findByNome(ParametroSistema.ANOS_VALIDADE_CARTAO)?:new ParametroSistema(nome:ParametroSistema.ANOS_VALIDADE_CARTAO,valor:"3").save(failOnError:true)
		ParametroSistema.findByNome(ParametroSistema.PEDIDO_VALIDADE)?:new ParametroSistema(nome:ParametroSistema.PEDIDO_VALIDADE,valor:"180").save(failOnError:true)
	}
}