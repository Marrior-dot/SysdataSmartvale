CREATE OR REPLACE FUNCTION public."inserirTransacaoCancelamento"

(
	dataHora timestamp with time zone,
	stsCtrl text,
	nsuHost integer,
	nsuTerm integer,
	valor numeric,
	codEst text,
	estId bigint,
	terminal text,
	fcnId bigint, 
	maqId bigint, 
	numcartao text, 
	crtId bigint, 
	codResp text 
	 
)
 RETURNS void
 LANGUAGE plpgsql
AS $function$

declare mngId integer;

begin

	select id into mngId from Motivo_Negacao where codigo = $13;

	insert into Transacao(
		id,
		date_created,
		data_hora,
		tipo,
		status,
		status_controle,
		nsu,
		nsu_terminal,
		valor,
		codigo_estabelecimento,
		estabelecimento_id,
		terminal,
		participante_id,
		maquina_id,
		numero_cartao,
		cartao_id,
		codigo_retorno,
		motivo_negacao_id
	)
	values(
		nextval('transacao_seq'),
		current_timestamp,
		$1,
		'CANCELAMENTO',
		'NAO_AGENDAR',
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
		mngId		
	);

end;$function$
;