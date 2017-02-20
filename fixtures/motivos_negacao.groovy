import com.sysdata.gestaofrota.MarcaVeiculo
import com.sysdata.gestaofrota.MotivoNegacao

fixture{
    println "CRIANDO MOTIVOS DE NEGAÇÃO..."
    dadosEstabelecimentos(MotivoNegacao,codigo:"01",descricao:"Estabelecimento inexistente")
    cartaoInexistente(MotivoNegacao,codigo:"02",descricao:"Cartão Inexistente")
    veiculoInexistente(MotivoNegacao,codigo:"03",descricao:"Veículo inexistente")
    relacaoFuncionarioVeiculoEquipamentoInexistente(MotivoNegacao,codigo:"04",descricao:"Relação Funcionário/Veículo ou Funcionário/Equipamento inexistente")
    precoInvalido(MotivoNegacao,codigo:"05",descricao:"Preço do combustível inválido")
    cartaoInativo(MotivoNegacao,codigo:"06",descricao:"Cartão inativo")
    matriculaInvalida(MotivoNegacao,codigo:"07",descricao:"Matrícula do funcionário inválida")
    qtdTransacaoDiariasInexistente(MotivoNegacao,codigo:"08",descricao:"Quantidade de Transações Diárias inexistente")
    combustivelInvalidoParaVeiculo(MotivoNegacao,codigo:"09",descricao:"Combustível inválido para Veículo")
    abastAcimaCapacidadeTanque(MotivoNegacao,codigo:"10",descricao:"Abastecimento acima da capacidade do tanque")
    foraLimiteAutonomia(MotivoNegacao,codigo:"11",descricao:"Fora do limite de autonomia do veículo")
    senhaInvalida(MotivoNegacao,codigo:"12",descricao:"Senha do cartão inválida")
    estabelecimentoInativo(MotivoNegacao,codigo:"13",descricao:"Estabelecimento inativo")
    funcionarioInativo(MotivoNegacao,codigo:"14",descricao:"Funcionário inativo")
    limiteTrnDiariasExcedido(MotivoNegacao,codigo:"15",descricao:"Limite de Transações Diárias Excedido")
    limiteDiasInatividadeExcedido(MotivoNegacao,codigo:"16",descricao:"Limite de Dias de Inatividade Excedido")
    diasInatividadeInexistente(MotivoNegacao,codigo:"17",descricao:"Dias de Inatividade inexistente")
    dadosTrnInexistente(MotivoNegacao,codigo:"18",descricao:"Dados da Transação inexistentes")
    relacaoProdutoEstabelecimentoInexistente(MotivoNegacao,codigo:"19",descricao:"Relação Produto/Estabelecimento inexistente")
    produtoInexistente(MotivoNegacao,codigo:"20",descricao:"Produto inexistente")
    precoProdutoInvalido(MotivoNegacao,codigo:"21",descricao:"Preços dos Produtos Invalidos")
    dadosTrnCancelamentoInexistente(MotivoNegacao,codigo:"22",descricao:"Dados da Transação de Cancelamento inexistentes")
    dadosTrnConfigPrecoInexistente(MotivoNegacao,codigo:"23",descricao:"Dados da Transação de Configuração de Preço inexistentes")
    excedeLimiteCartao(MotivoNegacao,codigo:"51",descricao:"Excede limite do cartão")
}