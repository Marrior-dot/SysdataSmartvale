package com.sysdata.gestaofrota

import grails.util.Holders

class CartaoService {
    Cartao gerar(Portador portador) {
        Cartao cartaoInstance = new Cartao()
        cartaoInstance.numero = gerarNumero(portador)
        cartaoInstance.senha = gerarSenha()
        cartaoInstance.validade = gerarDataValidade()
        cartaoInstance.portador = portador

        if (!cartaoInstance.save()) throw new RuntimeException("Erros de regras de negocio.")

        portador.addToCartoes(cartaoInstance)
        return cartaoInstance
    }
    Cartao desbloquear(Cartao cartao) {
        cartao.status = StatusCartao.ATIVO
        cartao.save()
    }
    Cartao cancelar(Cartao cartao, MotivoCancelamento motivo) {
        cartao.status = StatusCartao.CANCELADO
        cartao.motivoCancelamento = motivo

        final MotivoCancelamento[] motivosGerarNovoCartao = [MotivoCancelamento.ROUBO, MotivoCancelamento.DANIFICADO,
                                                             MotivoCancelamento.EXTRAVIO, MotivoCancelamento.PERDA]

        if (motivosGerarNovoCartao.contains(motivo)) {
            gerar(cartao.portador)
        } else if (motivo == MotivoCancelamento.SOLICITACAO_ADM) {
            //TODO VERIFICAR A VIABILIDADE DE SE COLOCAR UM STATUS EM Conta (CANCELA-LO AQUI).
//            cartao.portador.conta.status = ...
        }

        cartao.save()
    }

    private String gerarNumero(Portador portador) {
        def binPar = Administradora.list().first()
        def bin = binPar.bin
        def tipoProg = Holders.grailsApplication.config.project.tipoPrograma
        def parceiro = Holders.grailsApplication.config.project.parceiro
        def cdRh = portador.unidade.rh.id
        def qtde = portador.unidade.rh.qtdeContas
        def prov = sprintf("%6d%1d%1d%03d%07d", bin.toInteger(), tipoProg, parceiro, cdRh, ++qtde)
        def check = calcularDV(prov)
        portador.unidade.rh.qtdeContas = qtde
        portador.unidade.rh.save(flush: true)

        prov + check
    }
    private String gerarSenha() {
        Random random = new Random()
        int tamanho = (int) (Math.pow(10, Util.DIGITOS_SENHA) - 1)
        int senha = random.nextInt(tamanho)
        String fmt = "%0" + Util.DIGITOS_SENHA + "d"

        String.format(fmt, senha)
    }
    private Date gerarDataValidade() {
        Calendar cal = Calendar.getInstance()
        cal.setTime(new Date())
        Integer anosValidade = ParametroSistema.getValorAsInteger(ParametroSistema.ANOS_VALIDADE_CARTAO)
        cal.add(Calendar.YEAR, anosValidade)

        cal.getTime()
    }
    private String calcularDV(String card) {
        int valor;
        int soma = 0;
        int multiplicador = 1;
        int tamanho = card.length();
        for (int i = 0; i < tamanho; i++) {
            valor = Integer.parseInt(card.substring(i, i + 1)) * multiplicador;
            soma = soma + (valor / 10) + (valor % 10);
            if (multiplicador == 1)
                multiplicador = 2;
            else
                multiplicador = 1;
        }
        return String.valueOf((10 - (soma % 10)) % 10);
    }
}