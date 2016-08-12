import com.sysdata.gestaofrota.MarcaVeiculo
import com.sysdata.gestaofrota.MotivoNegacao

fixture{
    println "CRIANDO MOTIVOS DE NEGAÇÃO..."
    dadosEstabelecimentos(MotivoNegacao,codigo:"01",descricao:"Estabelecimento inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"02",descricao:"Cartão Inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"03",descricao:"Veículo inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"04",descricao:"Relação Funcionário/Veículo ou Funcionário/Equipamento inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"05",descricao:"Preço do combustível inválido")
    dadosEstabelecimentos(MotivoNegacao,codigo:"06",descricao:"Cartão inativo")
    dadosEstabelecimentos(MotivoNegacao,codigo:"07",descricao:"Matrícula do funcionário inválida")
    dadosEstabelecimentos(MotivoNegacao,codigo:"08",descricao:"Quantidade de Transações Diárias inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"09",descricao:"Combustível inválido para Veículo")
    dadosEstabelecimentos(MotivoNegacao,codigo:"10",descricao:"Abastecimento acima da capacidade do tanque")
    dadosEstabelecimentos(MotivoNegacao,codigo:"11",descricao:"Fora do limite de autonomia do veículo")
    dadosEstabelecimentos(MotivoNegacao,codigo:"12",descricao:"Senha do cartão inválida")
    dadosEstabelecimentos(MotivoNegacao,codigo:"13",descricao:"Estabelecimento inativo")
    dadosEstabelecimentos(MotivoNegacao,codigo:"14",descricao:"Funcionário inativo")
    dadosEstabelecimentos(MotivoNegacao,codigo:"15",descricao:"Limite de Transações Diárias Excedido")
    dadosEstabelecimentos(MotivoNegacao,codigo:"16",descricao:"Limite de Dias de Inatividade Excedido")
    dadosEstabelecimentos(MotivoNegacao,codigo:"17",descricao:"Dias de Inatividade inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"18",descricao:"Dados da Transação inexistentes")
    dadosEstabelecimentos(MotivoNegacao,codigo:"19",descricao:"Relação Produto/Estabelecimento inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"20",descricao:"Produto inexistente")
    dadosEstabelecimentos(MotivoNegacao,codigo:"21",descricao:"Preços dos Produtos Invalidos")
    dadosEstabelecimentos(MotivoNegacao,codigo:"22",descricao:"Dados da Transação de Cancelamento inexistentes")
    dadosEstabelecimentos(MotivoNegacao,codigo:"23",descricao:"Dados da Transação de Configuração de Preço inexistentes")
    dadosEstabelecimentos(MotivoNegacao,codigo:"51",descricao:"Dados Estabelecimentos")
}