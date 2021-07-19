import com.sysdata.gestaofrota.MarcaVeiculo
import com.sysdata.gestaofrota.MotivoNegacao

fixture{
    println "CRIANDO MOTIVOS DE NEGAÇÃO..."

    transacaoAprovada(MotivoNegacao,codigo: "00",descricao: "Transação Aprovada",ocorrencia: "Transação Aprovada")
    estabelecimentoBloqueado(MotivoNegacao,codigo: "02",descricao: "Estabelecimento Bloqueado",ocorrencia: "Estabelecimento Inativo")
    codEstabelecimentoInvalido(MotivoNegacao,codigo: "03",descricao: "Código do estabelecimento inválido",ocorrencia: "Código do estabelecimento inválido")
    erroInespecifico(MotivoNegacao,codigo: "06",descricao: "Erro Inespecífico",ocorrencia: "Erro Inespecífico")
    soliciteidentificação(MotivoNegacao,codigo: "08",descricao: "Solicite identificação",ocorrencia: "Solicite identificação")
    transacaoEmProcessamento(MotivoNegacao,codigo: "09",descricao: "Transação em processamento",ocorrencia: "Transação em andamento")
    transacaoInvalida(MotivoNegacao,codigo: "12",descricao: "Transação inválida",ocorrencia: "Transação inválida")
    valorInvalido(MotivoNegacao,codigo: "13",descricao: "Valor inválido",ocorrencia: "Relação produto x estabelecimento não encontrada / Produtos invalidos")
    probCartao14(MotivoNegacao,codigo: "14",descricao: "Prob. Cartão 14",ocorrencia: "Cartão inválido")
    instituicaoInvalida(MotivoNegacao,codigo: "15",descricao: "Instituição inválida",ocorrencia: "Instituição não cadastrada")
    refazerATransacao(MotivoNegacao,codigo: "19",descricao: "Refazer a transação",ocorrencia: "Refaça a transação")
    respostaInvalida(MotivoNegacao,codigo: "20",descricao: "Resposta inválida",ocorrencia: "Resposta inválida")
    registroNaoEncontrado(MotivoNegacao,codigo: "25",descricao: "Registro não encontrado",ocorrencia: "Dados da transação não encontrados")
    erroNoFormato(MotivoNegacao,codigo: "30",descricao: "Erro no formato",ocorrencia: "Erro no formato")
    instituicaoNaoPertenceARede(MotivoNegacao,codigo: "31",descricao: "Instituição não pertence à rede",ocorrencia: "Instituição invalida")
    probCartao34(MotivoNegacao,codigo: "34",descricao: "Prob. Cartão 34",ocorrencia: "Cartão LN fraude")
    excedeNumeroDeTentativas(MotivoNegacao,codigo: "38",descricao: "Excede número de tentativas",ocorrencia: "Excede número de tentativas")
    contaInvalida(MotivoNegacao,codigo: "39",descricao: "Conta inválida",ocorrencia: "Matrícula do funcionário inválida")
    probCartao41(MotivoNegacao,codigo: "41",descricao: "Prob. Cartão 41",ocorrencia: "Cartão perdido ou extraviado")
    probCartao43(MotivoNegacao,codigo: "43",descricao: "Prob. Cartão 43",ocorrencia: "Cartão roubado")
    excedeLimite(MotivoNegacao,codigo: "51",descricao: "Excede Limite",ocorrencia: "Excede limite do cartão")
    contaNaoCadastrado(MotivoNegacao,codigo: "52",descricao: "Conta não cadastrado",ocorrencia: "Relação Funcionário/Veículo ou Funcionário/Equipamento inexistente")
    cartaoVencido(MotivoNegacao,codigo: "54",descricao: "Cartão vencido",ocorrencia: "Cartão vencido")
    senhaIncorreta(MotivoNegacao,codigo: "55",descricao: "Senha incorreta",ocorrencia: "Senha do cartão inválida")
    cartaoNaoCadastrado(MotivoNegacao,codigo: "56",descricao: "Cartão não cadastrado",ocorrencia: "Cartão não cadastrado")
    transacaoNaoPermitida_Portador(MotivoNegacao,codigo: "57",descricao: "Transação não permitida/portador",ocorrencia: "Dados veiculo não encontrados")
    transacaoNaoPermitida_Terminal(MotivoNegacao,codigo: "58",descricao: "Transação não permitida/terminal",ocorrencia: "Transação não permitida/terminal")
    contaIrregular(MotivoNegacao,codigo: "59",descricao: "Conta irregular",ocorrencia: "Conta irregular")
    entreEmContato(MotivoNegacao,codigo: "60",descricao: "Entre em contato",ocorrencia: "Funcionário inativo")
    excedeLimiteDeSaque(MotivoNegacao,codigo: "61",descricao: "Excede limite de saque",ocorrencia: "Excede limite de saque")
    conta_CartaoBloqueado(MotivoNegacao,codigo: "62",descricao: "Conta/Cartão bloqueado",ocorrencia: "Cartão inativo")
    numSaquesInvalido(MotivoNegacao,codigo: "65",descricao: "Num saques inválido",ocorrencia: "Número de saques inválido")
    probCartao76(MotivoNegacao,codigo: "76",descricao: "Prob. Cartão 76",ocorrencia: "Cartão bloqueado")
    pendenteDeConfirmacao(MotivoNegacao,codigo: "77",descricao: "Pendente de confirmação",ocorrencia: "Trans pendente")
    transacaoCancelada(MotivoNegacao,codigo: "78",descricao: "Transação cancelada",ocorrencia: "Dados da transação de cancelamento não encontrados")
    transacaoNaoPermitida_Conta(MotivoNegacao,codigo: "79",descricao: "Transação não permitida/conta",ocorrencia: "Transação não permitida/conta")
    transacaoNaoExiste(MotivoNegacao,codigo: "80",descricao: "Transação não existe",ocorrencia: "Transação não existe")
    transacaoEstornada(MotivoNegacao,codigo: "81",descricao: "Transação estornada",ocorrencia: "Transação já estornada")
    chaveCriptograficaInvalida(MotivoNegacao,codigo: "82",descricao: "Chave criptográfica inválida",ocorrencia: "Variação de autonomia não cadastrada")
    timeOut(MotivoNegacao,codigo: "83",descricao: "Time out",ocorrencia: "Time out")
    logon_TerminalNaoAberto(MotivoNegacao,codigo: "84",descricao: "Logon (terminal não aberto)",ocorrencia: "Logon (terminal não aberto)")
    problemaNaRedeLocal(MotivoNegacao,codigo: "85",descricao: "Problema na rede local",ocorrencia: "Problema na rede local")
    transacaoDesfeita(MotivoNegacao,codigo: "86",descricao: "Transação desfeita",ocorrencia: "Transação desfeita")
    faltaNumCartao(MotivoNegacao,codigo: "87",descricao: "Falta num cartão",ocorrencia: "Dados portador não encontrados")
    sistemaFechado(MotivoNegacao,codigo: "90",descricao: "Sistema fechado",ocorrencia: "Sistema fechado")
    instituicaoTemporariamenteForaDeOperacao(MotivoNegacao,codigo: "91",descricao: "Instituição temporariamente fora de operação",ocorrencia: "Instituição temporariamente fora de operação")
    trilha2Invalida(MotivoNegacao,codigo: "96",descricao: "Trilha 2 inválida",ocorrencia: "Trilha 2 inválida")
    telefoneBloqueado(MotivoNegacao,codigo: "N0",descricao: "Telefone bloqueado",ocorrencia: "Telefone bloqueado")
    refacaATransacao(MotivoNegacao,codigo: "N1",descricao: "Refaça a transação",ocorrencia: "Refaça a transação")
    telefoneInvalido(MotivoNegacao,codigo: "N2",descricao: "Telefone inválido",ocorrencia: "Telefone inválido")
    valorInvalido(MotivoNegacao,codigo: "N3",descricao: "Valor inválido",ocorrencia: "Valor inválido")
    dddInvalido(MotivoNegacao,codigo: "S0",descricao: "DDD inválido",ocorrencia: "DDD inválido")
    versaoInvalida(MotivoNegacao,codigo: "S1",descricao: "Versão inválida",ocorrencia: "Versão inválida")
    codigoDeFilialInvalido(MotivoNegacao,codigo: "S3",descricao: "Código de filial inválido",ocorrencia: "Código de filial inválido")
}