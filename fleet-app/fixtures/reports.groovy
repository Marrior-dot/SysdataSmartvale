import br.com.acception.greport.DataType
import br.com.acception.greport.FieldReport
import br.com.acception.greport.MarkupType
import br.com.acception.greport.ParameterReport
import br.com.acception.greport.QueryType
import br.com.acception.greport.Report
import com.sysdata.gestaofrota.Role

println("CRIANDO REPORTS...")
fixture {
    projecaoPagarEstabelecimento(Report, name: "Projeção a Pagar para Estabelecimento", queryType: QueryType.SQL,
            query: "select p.id as idEstab,\n" +
                    "p.nome as razao,\n" +
                    "p.nome_fantasia,\n" +
                    "p.cnpj, \n" +
                    "to_char(l.data_efetivacao,'dd/MM/yyyy') as data_reembolso, \n" +
                    "sum(t.valor) as valor_original, \n" +
                    "sum(l.valor) as valor_bruto, \n" +
                    "coalesce((select b1.nome from Participante p1, Banco b1 where p.empresa_id=p1.id and p1.dado_bancario_banco_id=b1.id),'') as banco, \n" +
                    "coalesce((select p1.dado_bancario_agencia from Participante p1 where p.empresa_id=p1.id),'') as agencia, \n" +
                    "coalesce((select p1.dado_bancario_nome_titular from Participante p1 where p.empresa_id=p1.id),'') as titular_conta, \n" +
                    "coalesce((select p1.dado_bancario_documento_titular from Participante p1 where p.empresa_id=p1.id),'') as doc_titular, \n" +
                    "coalesce((select p1.dado_bancario_conta from Participante p1 where p.empresa_id=p1.id),'') as conta_corrente \n" +
                    "from Transacao t\n" +
                    "join Lancamento l on (t.id=l.transacao_id and l.tipo='REEMBOLSO' and l.status='A_EFETIVAR')\n" +
                    "join Participante p on (t.estabelecimento_id=p.id)\n" +
                    "where cast(l.data_efetivacao as date) >= cast(:dataInicio as date) \n" +
                    "and cast(l.data_efetivacao as date) <= cast(:dataFim as date) \n" +
                    "and ((t.codigo_estabelecimento like :codEstab||'%') or (cast('' as VARCHAR(255)) = :codEstab))\n" +
                    "group by 2,3,4,5,8,9,10,11,12,1 order by 5,2",
            countQuery: "select count(*) from \n" +
                    "(select p.id as idEstab,\n" +
                    "p.nome as razao,\n" +
                    "p.nome_fantasia,\n" +
                    "p.cnpj, \n" +
                    "to_char(l.data_efetivacao,'dd/MM/yyyy') as data_reembolso, \n" +
                    "sum(t.valor) as valor_original, \n" +
                    "sum(l.valor) as valor_bruto, \n" +
                    "coalesce((select b1.nome from Participante p1, Banco b1 where p.empresa_id=p1.id and p1.dado_bancario_banco_id=b1.id),'') as banco, \n" +
                    "coalesce((select p1.dado_bancario_agencia from Participante p1 where p.empresa_id=p1.id),'') as agencia, \n" +
                    "coalesce((select p1.dado_bancario_nome_titular from Participante p1 where p.empresa_id=p1.id),'') as titular_conta, \n" +
                    "coalesce((select p1.dado_bancario_documento_titular from Participante p1 where p.empresa_id=p1.id),'') as doc_titular, \n" +
                    "coalesce((select p1.dado_bancario_conta from Participante p1 where p.empresa_id=p1.id),'') as conta_corrente \n" +
                    "from Transacao t\n" +
                    "join Lancamento l on (t.id=l.transacao_id and l.tipo='REEMBOLSO' and l.status='A_EFETIVAR')\n" +
                    "join Participante p on (t.estabelecimento_id=p.id)\n" +
                    "where cast(l.data_efetivacao as date) >= cast(:dataInicio as date) \n" +
                    "and cast(l.data_efetivacao as date) <= cast(:dataFim as date) \n" +
                    "and ((t.codigo_estabelecimento like :codEstab||'%') or (cast('' as VARCHAR(255)) = :codEstab))\n" +
                    "group by 2,3,4,5,8,9,10,11,12,1 order by 5,2) as ddd",
            roles: [Role.findByAuthority("ROLE_ADMIN"), Role.findByAuthority("ROLE_PROC"), Role.findByAuthority("ROLE_ESTAB")]
    )

    estabelecimentosCadastrados(Report, name: "Estabelecimentos Cadastrados", queryType: QueryType.HQL,
            query: "select p.codigo as Cod_estabelecimento, " +
                    "p.nome as Razao_Social, " +
                    "p.nomeFantasia as Nome_Fantasia, " +
                    "p.cnpj as CNPJ, " +
                    "p.endereco.logradouro as Endereco, " +
                    "p.endereco.numero as Numero, " +
                    "p.endereco.complemento as Complemento, " +
                    "p.endereco.bairro as Bairro, " +
                    "c.nome as Cidade, " +
                    "e.nome as Estado, " +
                    "p.telefone.ddd as DDD, " +
                    "p.telefone.numero as Telefone, " +
                    "to_char(p.dateCreated,'DD/MM/YYYY') as Data_Cadastro, " +
                    "p.inscricaoEstadual as Inscricao_Estadual, " +
                    "p.inscricaoMunicipal as Inscricao_Municipal, " +
                    "(select b1.nome from Participante p1, " +
                    "Banco b1 where p.empresa.id=p1.id and p1.dadoBancario.banco.id=b1.id) as Banco, " +
                    "(select p1.dadoBancario.agencia from Participante p1 where p.empresa.id=p1.id) as Agencia, " +
                    "(select p1.dadoBancario.conta from Participante p1 where p.empresa.id=p1.id) as Conta, " +
                    "(select p1.dadoBancario.nomeTitular from Participante p1 where p.empresa.id=p1.id) as Titular_Conta " +
                    "from Participante p left outer join p.endereco.cidade c left outer join c.estado e where p.codigo is not null and " +
                    "p.class= 'com.sysdata.gestaofrota.Estabelecimento' and p.status='ATIVO' group by p.id, c.id, e.id order by p.codigo",
            countQuery: "select count(*) from Participante p " +
                    "left outer join p.endereco.cidade c left outer join c.estado e " +
                    "where p.codigo is not null and " +
                    "p.class= 'com.sysdata.gestaofrota.Estabelecimento' and p.status='ATIVO'",
            roles: [Role.findByAuthority("ROLE_ADMIN"), Role.findByAuthority("ROLE_PROC"), Role.findByAuthority("ROLE_HELP")]
    )

    transacaoCombustivelPeriodo(Report, name: "Transações Combustível por Período", queryType: QueryType.HQL,
            query: "select distinct t.numeroCartao as numero_cartao, " +
                    "(select p.matricula from Participante p where p=t.participante) as matricula, " +
                    "t.valor as valor, t.dateCreated as data_transacao, t.codigoEstabelecimento as codEstab," +
                    "t.terminal as terminal, " +
                    "(case when t.maquina = v then v.placa else e.codigo end) as placa_cod_equip, " +
                    "t.quilometragem as hodometro, t.statusControle as status," +
                    "(select mn.descricao from MotivoNegacao mn where mn.codigo=t.codigoRetorno) as negacao " +
                    "from Transacao t, Veiculo v, Equipamento e " +
                    "where t.tipo='COMBUSTIVEL' and (t.maquina=e or t.maquina=v) and " +
                    "cast(t.dateCreated as date) >= cast(:dataInicio as date) " +
                    "and cast(t.dateCreated as date) <= cast(:dataFim as date)  " +
                    "and t.codigoEstabelecimento like :codEstab || '%' and t.numeroCartao like :cartao " +
                    "|| '%' and v.placa like :placa || '%' and e.codigo like :cod_equip || '%' order by t.dateCreated desc",
            countQuery: "select count(*) from Transacao t, Veiculo v, Equipamento e " +
                    "where t.tipo='COMBUSTIVEL' and (t.maquina=e or t.maquina=v) " +
                    "and cast(t.dateCreated as date) >= cast(:dataInicio as date) " +
                    "and cast(t.dateCreated as date) <= cast(:dataFim as date) " +
                    "and t.codigoEstabelecimento like :codEstab || '%' and t.numeroCartao like :cartao || '%' \n" +
                    "and v.placa like :placa || '%' and e.codigo like :cod_equip || '%'",
            roles: Role.list()
    )

    detalhes(Report, name: "Detalhes Projeção Reembolso", queryType: QueryType.SQL,
            query: "select p.nome as razao, p.nome_fantasia, p.cnpj, " +
                    "to_char(l.data_efetivacao,'dd/MM/yyyy') as data_reembolso, t.numero_cartao as numero_cartao, " +
                    "t.valor as valor_original, l.valor as valor_bruto, t.date_created as data_transacao, " +
                    "t.codigo_estabelecimento as cod_estab, t.terminal as terminal, coalesce(m.placa,m.codigo) as placa_cod_equip, " +
                    "t.status_controle as status from transacao as t join lancamento l on (t.id=l.transacao_id) " +
                    "join Participante p on (t.estabelecimento_id=p.id) left outer join Maquina_Motorizada as m " +
                    "on (t.maquina_id=m.id) where t.tipo='COMBUSTIVEL' and l.tipo='REEMBOLSO' and l.status='A_EFETIVAR' and " +
                    "cast(l.data_efetivacao as date) = cast(:dataReembolso as date) and p.id=:idEstab " +
                    "group by 1,2,3,4,5,6,7,8,9,10,11,12 order by 4,1",
            countQuery: "select Count (*) from (select p.nome as razao, p.nome_fantasia, p.cnpj, " +
                    "to_char(l.data_efetivacao,'dd/MM/yyyy') as data_reembolso, t.numero_cartao as numero_cartao, " +
                    "t.valor as valor_original, l.valor as valor_bruto, t.date_created as data_transacao, " +
                    "t.codigo_estabelecimento as cod_estab, t.terminal as terminal, " +
                    "coalesce(m.placa,m.codigo) as placa_cod_equip, t.status_controle as status " +
                    "from transacao as t join lancamento l on (t.id=l.transacao_id) join Participante p " +
                    "on (t.estabelecimento_id=p.id) left outer join Maquina_Motorizada as m " +
                    "on (t.maquina_id=m.id) where t.tipo='COMBUSTIVEL' and l.tipo='REEMBOLSO' and l.status='A_EFETIVAR' " +
                    "and cast(l.data_efetivacao as date) = cast(:dataReembolso as date) and p.id=:idEstab " +
                    "group by 1,2,3,4,5,6,7,8,9,10,11,12 order by 4,1) as ddd",
            roles: []
    )
}

post {
    //PROJECAO A PAGAR PARA ESTABELECIMENTO =============================================================
    new FieldReport(name: "idEstab", label: "Id", order: 1, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "razao", label: "Razão Social", order: 2, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "nome_fantasia", label: "Nome Fantasia", order: 3, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "cnpj", label: "CNPJ", order: 4, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "data_reembolso", label: "Projeção de Reembolso", order: 5, dataType: DataType.STRING, totalizer: false, groupBy: true, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "valor_original", label: "Valor Original", order: 6, dataType: DataType.FLOAT, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "valor_bruto", label: "Valor Bruto", order: 7, dataType: DataType.FLOAT, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "banco", label: "Banco", order: 8, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "agencia", label: "Agência", order: 9, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "titular", label: "Titular", order: 10, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "doc_titular", label: "Doc. Titular", order: 11, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()
    new FieldReport(name: "conta_corrente", label: "C.C", order: 12, dataType: DataType.STRING, totalizer: false, groupBy: false, report: projecaoPagarEstabelecimento).save()

    def roles1 = [Role.findByAuthority("ROLE_ADMIN"), Role.findByAuthority("ROLE_PROC"), Role.findByAuthority("ROLE_ESTAB")]
    new ParameterReport(name: "dataInicio", label: "Data Início", dataType: DataType.DATE, markupType: MarkupType.DATE_PICKER, order: 1, mandatory: true, roles: roles1, report: projecaoPagarEstabelecimento).save()
    new ParameterReport(name: "dataFim", label: "Data Fim", dataType: DataType.DATE, markupType: MarkupType.DATE_PICKER, order: 2, mandatory: true, roles: roles1, report: projecaoPagarEstabelecimento).save()
    new ParameterReport(name: "codEstab", label: "Cod. Estabelecimento", dataType: DataType.STRING, markupType: MarkupType.TEXT, order: 3, mandatory: false, roles: [roles1[0], roles1[1]], report: projecaoPagarEstabelecimento).save()
    //===================================================================================================

    //ESTABELECIMENTOS CADASTRADOS ======================================================================
    new FieldReport(name: "Cod_Estabelecimento", label: "Cod Estab", order: 1, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Razao_Social", label: "Razão Social", order: 2, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Nome_Fantasia", label: "Nome Fantasia", order: 3, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "CNPJ", label: "CNPJ", order: 4, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Endereco", label: "Endereço", order: 5, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Numero", label: "Número", order: 6, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Complemento", label: "Complemento", order: 7, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Bairro", label: "Bairro", order: 8, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Cidade", label: "Cidade", order: 9, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Estado", label: "Estado", order: 10, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "DDD", label: "DDD", order: 11, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Telefone", label: "Telefone", order: 12, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Data_Cadastro", label: "Data de Cadastro", order: 13, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Inscricao_Estadual", label: "Inscrição Estadual", order: 14, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Inscricao_Municipal", label: "Inscrição Municipal", order: 15, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Banco", label: "Banco", order: 16, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Agencia", label: "Agência", order: 17, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Conta", label: "Conta", order: 18, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    new FieldReport(name: "Titular_Conta", label: "Titular da Conta", order: 19, dataType: DataType.STRING, totalizer: false, groupBy: false, report: estabelecimentosCadastrados).save()
    //===================================================================================================


    //TRANSACAO COMBUSTIVEL POR PERIODO =================================================================
    new FieldReport(name: "numeroCartao", label: "Cartão", order: 1, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "matricula", label: "Matrícula", order: 2, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "valor", label: "Valor", order: 3, dataType: DataType.FLOAT, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "data_transacao", label: "Data Hora", order: 4, dataType: DataType.TIMESTAMP, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "cod_estab", label: "Cod Estab", order: 5, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "terminal", label: "Terminal", order: 6, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "placa_cod_equip", label: "Placa/Cod.Equip.", order: 7, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "hodometro", label: "Hodômetro", order: 8, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "status", label: "Status", order: 9, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()
    new FieldReport(name: "negacao", label: "Negação", order: 10, dataType: DataType.STRING, totalizer: false, groupBy: false, report: transacaoCombustivelPeriodo).save()

    def allRoles = Role.list()
    new ParameterReport(name: "dataInicio", label: "Início", dataType: DataType.DATE, markupType: MarkupType.DATE_PICKER, order: 1, mandatory: true, roles: allRoles, report: transacaoCombustivelPeriodo).save()
    new ParameterReport(name: "dataFim", label: "Fim", dataType: DataType.DATE, markupType: MarkupType.DATE_PICKER, order: 2, mandatory: true, roles: allRoles, report: transacaoCombustivelPeriodo).save()
    new ParameterReport(name: "cartao", label: "Cartão", dataType: DataType.STRING, markupType: MarkupType.TEXT, order: 3, mandatory: false, roles: allRoles, report: transacaoCombustivelPeriodo).save()
    new ParameterReport(name: "codEstab", label: "Cód. Estab.", dataType: DataType.STRING, markupType: MarkupType.TEXT, order: 4, mandatory: false, roles: allRoles - roles1[2], report: transacaoCombustivelPeriodo).save()
    new ParameterReport(name: "placa", label: "Placa (ex: ABC-1234)", dataType: DataType.STRING, markupType: MarkupType.TEXT, order: 5, mandatory: false, roles: allRoles, report: transacaoCombustivelPeriodo).save()
    new ParameterReport(name: "cod_equip", label: "Cod. Equipamento", dataType: DataType.STRING, markupType: MarkupType.TEXT, order: 6, mandatory: false, roles: allRoles, report: transacaoCombustivelPeriodo).save()
    //===================================================================================================

    //DETALHES ==========================================================================================
    new FieldReport(name: "razao", label: "Razão Social", order: 1, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "nome_fantasia", label: "Nome Fantasia", order: 2, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "cnpj", label: "CNPJ", order: 3, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "data_reembolso", label: "Projeção de Reembolso", order: 4, dataType: DataType.STRING, totalizer: false, groupBy: true, report: detalhes).save()
    new FieldReport(name: "numero_cartao", label: "Cartão", order: 5, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "valor_original", label: "Valor Original", order: 6, dataType: DataType.FLOAT, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "valor_bruto", label: "Valor Bruto", order: 7, dataType: DataType.FLOAT, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "data_transacao", label: "Data Hora", order: 8, dataType: DataType.TIMESTAMP, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "cod_estab", label: "Cod Estab", order: 9, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "terminal", label: "Terminal", order: 10, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "placa_cod_equip", label: "Placa/Cod.Equip.", order: 11, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()
    new FieldReport(name: "status", label: "Status", order: 12, dataType: DataType.STRING, totalizer: false, groupBy: false, report: detalhes).save()

    new ParameterReport(name: "idEstab", label: "IdEstab", dataType: DataType.INTEGER, markupType: MarkupType.TEXT, order: 1, mandatory: false, roles: allRoles, report: detalhes).save()
    new ParameterReport(name: "dataReembolso", label: "Data Reembolso", dataType: DataType.DATE, markupType: MarkupType.TEXT, order: 2, mandatory: false, roles: allRoles, report: detalhes).save()
    //===================================================================================================
}