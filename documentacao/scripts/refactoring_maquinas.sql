
/* Copiar dados da tabela VEICULO para a tabela MAQUINA_MOTORIZADA */
insert into MAQUINA_MOTORIZADA(id,version,capacidade_tanque,tipo_abastecimento,unidade_id,class,ano_fabricacao,autonomia,chassi,
marca_id,modelo,placa,validade_extintor) 
select id,version,capacidade_tanque,tipo_abastecimento,unidade_id,'com.sysdata.gestaofrota.Veiculo',ano_fabricacao,autonomia,chassi,
marca_id,modelo,placa,validade_extintor 
from VEICULO order by id;

/* Definir valor da sequência MAQUINA_SEQ para o mesmo valor de VEICULO_SEQ */

SELECT setval('maquina_seq', (select max(id)+1 from maquina_motorizada), FALSE);

/* Remover sequência VEICULO_SEQ */
drop sequence VEICULO_SEQ;

/* Copiar dados da tabela VEICULO_FUNCIONARIO para a tabela MAQUINA_FUNCIONARIO */
insert into MAQUINA_FUNCIONARIO(id,version,date_created,funcionario_id,maquina_id,status) 
select id,version,date_created,funcionario_id,veiculo_id,status from VEICULO_FUNCIONARIO order by id;

/* Definir valor da sequência MAQFUNC_SEQ para o ID máximo do campo chave da tabela VEICULO_FUNCIONARIO */
SELECT setval('maqfunc_seq', (select max(id)+1 from maquina_funcionario), FALSE);

/* Copiar dados da coluna VEICULO_ID da tabela TRANSACAO para a coluna MAQUINA_ID */
update TRANSACAO set maquina_id=veiculo_id;

/* Remover coluna VEICULO_ID da tabela TRANSACAO */
alter table TRANSACAO drop VEICULO_ID;

/* Remover tabela VEICULO_FUNCIONARIO */
drop table VEICULO_FUNCIONARIO;

/* Remover tabela VEICULO */
drop table VEICULO;

/* Remover tabela EQUIPAMENTO */
drop table EQUIPAMENTO;

drop sequence EQUIPAMENTO_SEQ;

/* Definir todos os funcionários como não gestor */
update PARTICIPANTE set gestor=false where class='com.sysdata.gestaofrota.Funcionario'


