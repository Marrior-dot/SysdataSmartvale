import com.sysdata.gestaofrota.ParametroSistema

fixture {
	println("CRIANDO PARAMETROS...")
	paramBin(ParametroSistema, nome: ParametroSistema.BIN, valor: "623409")
	paramValidadeCartao(ParametroSistema, nome: ParametroSistema.ANOS_VALIDADE_CARTAO, valor: "3")
	paramValidadePedido(ParametroSistema, nome: ParametroSistema.PEDIDO_VALIDADE, valor: "180")
}