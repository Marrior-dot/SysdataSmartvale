CREATE OR REPLACE FUNCTION public."inserirTransacaoCompra"(prtid bigint, valor numeric, "codEst" text, "estId" bigint, terminal text, placa text, maqid bigint, numcartao text, crtid bigint, "codResp" text, "nsuHost" integer, "nsuTerm" integer, "stsCtrl" text, km bigint, "dataHora" timestamp with time zone, motneg text, prcunit numeric, codigoequip text, qtd_litros numeric, prods text, tipotrn text)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

declare trnId integer;
declare ret boolean;
declare mngId integer;

begin

	trnId = nextval('transacao_seq');
	
	select id into mngId from Motivo_Negacao where codigo = $16;

	insert into Transacao(
		id,
		date_created,
		status,
		participante_id,
		valor,
		codigo_estabelecimento,
		estabelecimento_id,
		terminal,
		placa,
		maquina_id,
		numero_cartao,
		cartao_id,
		codigo_retorno,
		nsu,
		nsu_terminal,
		status_controle,
		quilometragem,
		data_hora,
		motivo_negacao_id,
		preco_unitario,
		codigo_equipamento,
		qtd_litros,
		tipo
	)
	values(
		trnId,
		current_timestamp,
		'NAO_AGENDAR',
		$1, 
		$2,   
		$3, 
		$4, 
		$5, 
		$6, 
		$7, 
		$8, 
		$9, 
		$10, 
		$11, 
		$12, 
		$13, 
		$14, 
		$15, 
		mngId, 
		$17, 
		$18, 
		$19,
		$21  
	);


	select "inserirProdutoTransacao"(prods,trnId,"estId") into ret;



end;$function$
;