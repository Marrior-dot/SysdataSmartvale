package com.sysdata.gestaofrota

import com.fourLions.processingControl.BatchProcessing
import com.fourLions.processingControl.DailySchedule
import com.fourLions.processingControl.ExecutionFrequency
import com.fourLions.processingControl.Processing
import grails.core.GrailsApplication
import grails.gorm.transactions.Transactional

@Transactional
class FixturesService {

    GrailsApplication grailsApplication

    def init() {
        criarPerfisAcesso()
        criarProcessadora()
        //criarAdministradora()
        criarAutenticacaoInicial()
        criarEstadosCapitais()
        criarBancos()
        criarProcessamentos()
        criarMarcasVeiculos()
        criarMotivosNegacao()
    }

    private void criarPerfisAcesso() {
        Role.findOrCreateWhere(authority: 'ROLE_PROC').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_ADMIN').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_RH').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_ESTAB').save(flush: true)
        Role.findOrCreateWhere(authority: 'ROLE_PROC_FINANC', description: 'Perfil do Financeiro Processadora').save(flush: true)
    }


    private def criarProcessadora() {
        Processadora.findOrCreateWhere(nome: "Sysdata Tecnologia").save(flush: true)
        assert Processadora.count() == 1
    }

    private def criarAdministradora() {
        def adm = grailsApplication.config.projeto.administradora
        Administradora.findOrCreateWhere([bin: adm.bin, nome: adm.nome]).save(flush: true)
        assert Administradora.count() == 1
        ParametroSistema.findOrCreateWhere([nome: ParametroSistema.ANOS_VALIDADE_CARTAO, valor: adm.anosValidadeCartao.toString(), escopo: EscopoParametro.ADMINISTRADORA, tipoDado: TipoDado.INTEGER]).save(flush: true)
    }


    private def criarAutenticacaoInicial() {
        Role startRole = Role.findOrCreateWhere(authority: "ROLE_PROC").save()
        User startUser
        if (! User.findByUsername("sys.start")) {
            startUser = new User(username: "sys.start", password: "jmml72", owner: Processadora.first())
            startUser.save()
            UserRole.create startUser, startRole
            log.info "Start User criado"
        }
    }

    private def criarEstadosCapitais() {
        Estado acre = Estado.findOrCreateWhere([uf: "AC", nome: "ACRE"]).save()
        Estado alagoas = Estado.findOrCreateWhere([uf: "AL", nome: "ALAGOAS"]).save()
        Estado amazonas = Estado.findOrCreateWhere([uf: "AM", nome: "AMAZONAS"]).save()
        Estado amapa = Estado.findOrCreateWhere([uf: "AP", nome: "AMAPÁ"]).save()
        Estado bahia = Estado.findOrCreateWhere([uf: "BA", nome: "BAHIA"]).save()
        Estado ceara = Estado.findOrCreateWhere([uf: "CE", nome: "CEARÁ"]).save()
        Estado df = Estado.findOrCreateWhere([uf: "DF", nome: "DISTRITO FEDERAL"]).save()
        Estado espSanto = Estado.findOrCreateWhere([uf: "ES", nome: "ESPÍRITO SANTO"]).save()
        Estado goias = Estado.findOrCreateWhere([uf: "GO", nome: "GOIÁS"]).save()
        Estado maranhao = Estado.findOrCreateWhere([uf: "MA", nome: "MARANHÃO"]).save()
        Estado minas = Estado.findOrCreateWhere([uf: "MG", nome: "MINAS GERAIS"]).save()
        Estado matoGrossoSul = Estado.findOrCreateWhere([uf: "MS", nome: "MATO GROSSO DO SUL"]).save()
        Estado matoGrosso = Estado.findOrCreateWhere([uf: "MT", nome: "MATO GROSSO"]).save()
        Estado para = Estado.findOrCreateWhere([uf: "PA", nome: "PARÁ"]).save()
        Estado paraiba = Estado.findOrCreateWhere([uf: "PB", nome: "PARAÍBA"]).save()
        Estado pernambuco = Estado.findOrCreateWhere([uf: "PE", nome: "PERNAMBUCO"]).save()
        Estado piaui = Estado.findOrCreateWhere([uf: "PI", nome: "PIAUÍ"]).save()
        Estado parana = Estado.findOrCreateWhere([uf: "PR", nome: "PARANÁ"]).save()
        Estado rio = Estado.findOrCreateWhere([uf: "RJ", nome: "RIO DE JANEIRO"]).save()
        Estado rioGrandeNorte = Estado.findOrCreateWhere([uf: "RN", nome: "RIO GRANDE DO NORTE"]).save()
        Estado rondonia = Estado.findOrCreateWhere([uf: "RO", nome: "RONDÔNIA"]).save()
        Estado rioGrandeSul = Estado.findOrCreateWhere([uf: "RS", nome: "RIO GRANDE DO SUL"]).save()
        Estado roraima = Estado.findOrCreateWhere([uf: "RR", nome: "RORAIMA"]).save()
        Estado santaCatarina = Estado.findOrCreateWhere([uf: "SC", nome: "SANTA CATARINA"]).save()
        Estado sergipe = Estado.findOrCreateWhere([uf: "SE", nome: "SERGIPE"]).save()
        Estado saoPaulo = Estado.findOrCreateWhere([uf: "SP", nome: "SÃO PAULO"]).save()
        Estado tocantins = Estado.findOrCreateWhere([uf: "TO", nome: "TOCANTINS"]).save()

        Cidade.findOrCreateWhere([nome: "RIO BRANCO", estado: acre]).save()
        Cidade.findOrCreateWhere([nome: "MACEIÓ", estado: alagoas]).save()
        Cidade.findOrCreateWhere([nome: "MANAUS", estado: amazonas]).save()
        Cidade.findOrCreateWhere([nome: "MACAPÁ", estado: amapa]).save()
        Cidade.findOrCreateWhere([nome: "SALVADOR", estado: bahia]).save()
        Cidade.findOrCreateWhere([nome: "FORTALEZA", estado: ceara]).save()
        Cidade.findOrCreateWhere([nome: "BRASÍLIA", estado: df]).save()
        Cidade.findOrCreateWhere([nome: "VITÓRIA", estado: espSanto]).save()
        Cidade.findOrCreateWhere([nome: "GOIÂNIA", estado: goias]).save()
        Cidade.findOrCreateWhere([nome: "SÃO LUÍS", estado: maranhao]).save()
        Cidade.findOrCreateWhere([nome: "BELO HORIZONTE", estado: minas]).save()
        Cidade.findOrCreateWhere([nome: "CAMPO GRANDE", estado: matoGrossoSul]).save()
        Cidade.findOrCreateWhere([nome: "CUIABÁ", estado: matoGrosso]).save()
        Cidade.findOrCreateWhere([nome: "BELÉM", estado: para]).save()
        Cidade.findOrCreateWhere([nome: "JOÃO PESSOA", estado: paraiba]).save()
        Cidade.findOrCreateWhere([nome: "RECIFE", estado: pernambuco]).save()
        Cidade.findOrCreateWhere([nome: "TERESINA", estado: piaui]).save()
        Cidade.findOrCreateWhere([nome: "CURITIBA", estado: parana]).save()
        Cidade.findOrCreateWhere([nome: "RIO DE JANEIRO", estado: rio]).save()
        Cidade.findOrCreateWhere([nome: "NATAL", estado: rioGrandeNorte]).save()
        Cidade.findOrCreateWhere([nome: "PORTO VELHO", estado: rondonia]).save()
        Cidade.findOrCreateWhere([nome: "PORTO ALEGRE", estado: rioGrandeSul]).save()
        Cidade.findOrCreateWhere([nome: "BOA VISTA", estado: roraima]).save()
        Cidade.findOrCreateWhere([nome: "FLORIANÓPOLIS", estado: santaCatarina]).save()
        Cidade.findOrCreateWhere([nome: "ARACAJÚ", estado: sergipe]).save()
        Cidade.findOrCreateWhere([nome: "SÃO PAULO", estado: saoPaulo]).save()
        Cidade.findOrCreateWhere([nome: "PALMAS", estado: tocantins]).save()
    }

    private def criarBancos() {
        Banco.findOrCreateWhere([codigo: "1", nome: "BANCO DO BRASIL S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "104", nome: "CAIXA ECONOMICA FEDERAL"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "33", nome: "BANCO SANTANDER BRASIL S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "237", nome: "BANCO BRADESCO S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "341", nome: "BANCO ITAU S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "37", nome: "BANCO DO ESTADO DO PARÁ S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "748", nome: "BANCO COOPERATIVO SICRED S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "003", nome: "BANCO DA AMAZONIA S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "380", nome: "PICPAY SERVICOS S.A"]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "389", nome: "Banco Mercantil do Brasil S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "745", nome: "Banco Citibank S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "243", nome: "Banco Máxima S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "422", nome: "Banco Safra S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "623", nome: "Banco Pan S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "655", nome: "Banco Votorantim S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "069", nome: "Banco Crefisa S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "318", nome: "Banco BMG S.A."]).save(flush: true)
        Banco.findOrCreateWhere([codigo: "070", nome: "BRB – Banco de Brasília S.A"]).save(flush: true)
    }

    private def criarProcessamentos() {
/*
        if (grailsApplication.config.projeto.projectId == "bahiavale")
            Processing.findOrCreateWhere([name: "Geração Arquivo NFe - RPS Barueri", order: 6 as byte, service: "geracaoArquivoRPSBarueriService", active: true, batch: batch]).save(flush: true)
*/

        if (grailsApplication.config.projeto.projectId == "banpara") {

            BatchProcessing batchCorte = BatchProcessing.findByName("Corte Diário")
            if (! batchCorte) {
                batchCorte = new BatchProcessing([name: "Corte Diário",
                                                  active: true,
                                                  executionSchedule: new DailySchedule(hour: 22,
                                                                                        minute: 0,
                                                                                        executionFrequency: ExecutionFrequency.DAILY)])
                batchCorte.save(flush: true)
            }

            Processing.findOrCreateWhere([name: "Agendamento de Transações", order: 1 as byte,
                                          service: "agendamentoTransacaoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Estabelecimento", order: 2 as byte,
                                          service: "corteReembolsoEstabsService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Fechamento de Lote Pagamento", order: 3 as byte,
                                          service: "fechamentoLotePagamentoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Fechamento de Lote Recebimento", order: 4 as byte,
                                          service: "fechamentoLoteRecebimentoService", active: true,
                                          batch: batchCorte]).save(flush: true)

            Processing.findOrCreateWhere([name: "Envio Lote Recebimento Banpará API", order: 5 as byte,
                                          service: "enviarLoteRecebimentoAPIBanparaService", active: true,
                                          batch: batchCorte]).save(flush: true)


            BatchProcessing batch = BatchProcessing.findByName("Processamento Diário")
            if (! batch) {
                batch = new BatchProcessing([name: "Processamento Diário",
                                             active: true,
                                             executionSchedule: new DailySchedule(hour: 2, minute: 0, executionFrequency: ExecutionFrequency.DAILY)])
                batch.save(flush: true)
            }

            Processing.findOrCreateWhere([name: "Carga de Pedidos", order: 1 as byte,
                                          service: "cargaPedidoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Pedidos de Carga", order: 2 as byte,
                                          service: "faturamentoCargaPedidoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Faturamento Portador", order: 3 as byte,
                                          service: "faturamentoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Geração Arquivo Embossing", order: 4 as byte,
                                          service: "geracaoArquivoEmbossingService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Atualização de Saldos", order: 5 as byte,
                                          service: "atualizacaoSaldoService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Consulta Lote Banpará API", order: 6 as byte,
                                          service: "consultarLoteAPIBanparaService", active: true,
                                          batch: batch]).save(flush: true)

            Processing.findOrCreateWhere([name: "Consulta Lotes Devolvidos - Banpará API", order: 7 as byte,
                                          service: "consultarLoteDevolucaoAPIBanparaService", active: true,
                                          batch: batch]).save(flush: true)




            //Processing.findOrCreateWhere([name: "Envio Lote Banpará API", order: 11 as byte, service: "enviarLoteAPIBanparaService", active: true, batch: batch]).save(flush: true)
        }

        if (grailsApplication.config.projeto.projectId == "smartvale") {
            //Processing.findOrCreateWhere([name: "Faturamento de Ordens de Serviços - Omie API", order: 10 as byte, service: "faturamentoOrdemServiceOmieService", active: true, batch: batch]).save(flush: true)
        }

        Processing.findOrCreateWhere([name: "Recálculo Saldos de Clientes", order: 1 as byte,
                                      service: "recalculoSaldosConveniosService", active: true])
                                      .save(flush: true)

        Processing.findOrCreateWhere([name: "Geração Cartões Provisórios", order: 2 as byte,
                                      service: "processadorSolicitacaoCartaoProvisorioService", active: true])
                                      .save(flush: true)

    }

    private def criarMarcasVeiculos() {

        MarcaVeiculo.findOrCreateWhere([nome: "CHEVROLET", abreviacao: 'CHEVY']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "VOLKSWAGEM", abreviacao: 'VW']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "FIAT", abreviacao: 'FIAT']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "FORD", abreviacao: 'FORD']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "RENAULT", abreviacao: 'REN']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "TOYOTA", abreviacao: 'TYO']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "HONDA", abreviacao: 'HON']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "HYUNDAI", abreviacao: 'HYU']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "NISSAN", abreviacao: 'NIS']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "PEUGEOT", abreviacao: 'PGT']).save(flush: true)
        MarcaVeiculo.findOrCreateWhere([nome: "CITROËN", abreviacao: 'CIT']).save(flush: true)
    }
    
    private def criarMotivosNegacao() {

        MotivoNegacao.findOrCreateWhere([codigo: "01", descricao: "Transação inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "02", descricao: "Estabelecimento inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "03", descricao: "Produto inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "04", descricao: "Produto não vinculado ao EC"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "05", descricao: "Funcionário inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "06", descricao: "Veículo inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "07", descricao: "Cartão inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "08", descricao: "Portador inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "09", descricao: "Veículo não vinculado ao Funcionário"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "11", descricao: "Estabelecimento bloqueado"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "12", descricao: "Preço inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "13", descricao: "Cartão não ativo"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "14", descricao: "Combustível inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "15", descricao: "Quantidade combustível inválida"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "17", descricao: "Funcionário não ativo"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "18", descricao: "Equipamento inexistente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "19", descricao: "Equipamento não vinculado ao Funcionário"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "20", descricao: "Produtos inválidos"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "21", descricao: "Intervalo de Tempo inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "22", descricao: "Intervalo Percorrido inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "23", descricao: "Horário inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "24", descricao: "Qtde litros abastecidos inválida"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "25", descricao: "Dia semana inválido"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "26", descricao: "EC não habilitado à Empresa Cliente"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "51", descricao: "Saldo indisponível"]).save(flush: true)
        MotivoNegacao.findOrCreateWhere([codigo: "55", descricao: "Senha inválida"]).save(flush: true)
    }
}
