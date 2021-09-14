ALTER TABLE public.portador ALTER COLUMN unidade_id DROP NOT NULL;
ALTER TABLE public.lote_embossing ALTER COLUMN usuario_id DROP NOT NULL;

update cartao set tipo = 'PADRAO' where tipo is null;

update lote_embossing set tipo = 'PADRAO' where tipo is null;