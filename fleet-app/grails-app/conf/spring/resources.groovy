import com.sysdata.gestaofrota.UserPasswordEncoderListener

// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
    geradorCartao(application.config.projeto.cartao.gerador)
}
