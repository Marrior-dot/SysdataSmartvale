import com.sysdata.gestaofrota.UserPasswordEncoderListener
import com.sysdata.gestaofrota.processamento.administradoras.Sysdata

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    geradorCartao(application.config.projeto.cartao.gerador)
}
