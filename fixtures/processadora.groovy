import com.sysdata.gestaofrota.Administradora
import com.sysdata.gestaofrota.Processadora

fixture {
	println("CRIANDO ADMINISTRADORA...")
	partAdministradora(Administradora, nome: "AMAZON", bin: "623409")

	println("CRIANDO PROCESSADORA...")
	partProcessadora(Processadora, nome: "SYSDATA")
}