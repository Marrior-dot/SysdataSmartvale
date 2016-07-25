import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Banco
import com.sysdata.gestaofrota.Cidade
import com.sysdata.gestaofrota.Estado
import com.sysdata.gestaofrota.MarcaVeiculo
import com.sysdata.gestaofrota.ParametroSistema
import com.sysdata.gestaofrota.Processadora
import com.sysdata.gestaofrota.Role

class BootStrap {
    def fixtureLoader

    def init = { servletContext ->

//        Date.metaClass.trunc = { ->
//            Calendar cal = Calendar.instance
//            cal.time = delegate
//            cal.set(Calendar.HOUR_OF_DAY, 0)
//            cal.set(Calendar.MINUTE, 0)
//            cal.set(Calendar.SECOND, 0)
//            cal.time
//        }

//		switch(Environment.current){
//			case Environment.DEVELOPMENT:
//        createDefaultOwners()
//        createDefaultUser()
//        createParameters()
//				break;
//		}


//        fixtureLoader.load {
//            partAdministradora(Administradora, nome: "AMAZON", bin: "623409")
//        }


        loadFixtures()
    }

    def destroy = {}


    def loadFixtures() {
        if (Administradora.count() == 0 || Processadora.count() == 0)
            fixtureLoader.load("processadora")

        if (Role.count() == 0)
            fixtureLoader.load("users")
        if (Banco.count() == 0)
            fixtureLoader.load("bancos")
        if(ParametroSistema.count == 0)
            fixtureLoader.load("parametros")

        if (Estado.count() == 0)
            fixtureLoader.load("all_estados")
        if (Cidade.count() == 0) {
            fixtureLoader.load("cidades_ab")
            fixtureLoader.load("cidades_cde")
            fixtureLoader.load("cidades_gm")
            fixtureLoader.load("cidades_p")
            fixtureLoader.load("cidades_r")
            fixtureLoader.load("cidades_st")
        }

        if(MarcaVeiculo.count==0){
            fixtureLoader.load("marcas_veiculo")
        }
    }

//	void createDefaultOwners(){
//		Administradora.count()==0? new Administradora(nome:"AMAZON",bin:"623409").save(failOnError:true):false
//		Processadora.count()==0? new Processadora(nome:"SYSDATA").save(failOnError:true):false
//	}

//	void createDefaultUser(){
//		def roleProc=Role.findByAuthority('ROLE_PROC')?:new Role(authority:'ROLE_PROC').save(failOnError:true)
//		def roleAdmin=Role.findByAuthority('ROLE_ADMIN')?:new Role(authority:'ROLE_ADMIN').save(failOnError:true)
//		def roleRh=Role.findByAuthority('ROLE_RH')?:new Role(authority:'ROLE_RH').save(failOnError:true)
//		def roleEstab=Role.findByAuthority('ROLE_ESTAB')?:new Role(authority:'ROLE_ESTAB').save(failOnError:true)
//
//		def procInstance=Processadora.findByNome("SYSDATA")
//		def adminInstance=Administradora.findByNome("AMAZON")
//
//		def adminUser=User.findByUsername('admin')?:new User(owner:procInstance,name:'USUARIO PROC ADMIN',username:'admin',password:'fr07@',enabled:true).save(failOnError:true)
//		def luizUser=User.findByUsername('luiz.leao')?:new User(owner:procInstance,name:'LUIZ LEAO',username:'luiz.leao',password:'123',enabled:true).save(failOnError:true)
//
//		if(!luizUser.authorities.contains(roleProc))
//			UserRole.create luizUser,roleProc
//		if(!luizUser.authorities.contains(roleAdmin))
//			UserRole.create luizUser,roleAdmin
//
//		if(!adminUser.authorities.contains(roleProc))
//			UserRole.create adminUser,roleProc
//		if(!adminUser.authorities.contains(roleAdmin))
//			UserRole.create adminUser,roleAdmin
//
//	}

//    void createParameters() {
//        ParametroSistema.findByNome(ParametroSistema.BIN) ?: new ParametroSistema(nome: ParametroSistema.BIN, valor: "623409").save(failOnError: true)
//        ParametroSistema.findByNome(ParametroSistema.ANOS_VALIDADE_CARTAO) ?: new ParametroSistema(nome: ParametroSistema.ANOS_VALIDADE_CARTAO, valor: "3").save(failOnError: true)
//        ParametroSistema.findByNome(ParametroSistema.PEDIDO_VALIDADE) ?: new ParametroSistema(nome: ParametroSistema.PEDIDO_VALIDADE, valor: "180").save(failOnError: true)
//    }
}
