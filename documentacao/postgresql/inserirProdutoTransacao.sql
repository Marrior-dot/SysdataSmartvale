CREATE OR REPLACE FUNCTION public."inserirProdutoTransacao"(prods text, trnid integer, estid bigint)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$

declare countProds int;
declare ind int;
declare codProd text;
declare startPos int;
declare prdId bigint;
declare price numeric(19,2);

begin

if (prods is not null) then

  countProds = char_length(prods);
  
  if (countProds % 2 = 0) then
    countProds = char_length(prods) / 2;
    startPos := 1;
    for ind in 1..countProds loop
      codProd := substr(prods, startPos, 2);
      startPos := startPos + 2;
    

      select p.id,pe.valor 
      into prdId,price 
      from PRODUTO p, PRODUTO_ESTABELECIMENTO pe 
      where codigo = codProd
      and pe.estabelecimento_id = $3
      and p.id = pe.produto_id;

      if( prdId is not null ) then
    	 insert into TRANSACAO_PRODUTO values(nextval('transacaoprod_seq'), price, prdId, trnId);
      end if;
    

    end loop;
    return true;
  else
    return false;
  end if;
else
  return false;
end if;


end;$function$
;
