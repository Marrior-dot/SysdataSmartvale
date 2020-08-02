CREATE OR REPLACE FUNCTION public."validarProdutosPorEstab"(codest text, prods text)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$

declare countProds int;
declare ind int;
declare codProd text;
declare startPos int;
declare prdId bigint;
declare exist boolean;

begin

  exist = true;

  if (prods is not null) then

    countProds = char_length(prods);
  
    if (countProds % 2 = 0) then
      countProds = char_length(prods) / 2;
      startPos := 1;
      for ind in 1..countProds loop
        codProd := substr(prods, startPos, 2);
        startPos := startPos + 2;
    

        select p.id into prdId 
        from PRODUTO p, PRODUTO_ESTABELECIMENTO pe, PARTICIPANTE e 
        where pe.estabelecimento_id = e.id
        and p.id = pe.produto_id
        and e.codigo = $1
        and p.codigo = codProd;

        exist = exist and (prdId is not null);

      end loop;
      return exist;
    else
      return false;
    end if;
  end if;

  return false;

end;
$function$
;