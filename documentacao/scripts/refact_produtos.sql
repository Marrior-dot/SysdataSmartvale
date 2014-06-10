insert into PRODUTO_ESTABELECIMENTO
select pc.id,pc.estabelecimento_id,p.id,valor,valor_anterior 
from preco_combustivel pc, produto p
where pc.tipo_combustivel=p.nome
order by pc.id

select setval('produtoestab_seq', (select max(id)+1 from PRODUTO_ESTABELECIMENTO), FALSE);

