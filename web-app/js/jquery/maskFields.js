$(function() {
	$("input[name='cpf']").inputmask("999.999.999-99",{placeholder:" "});
	$("input[name='cnpj']").inputmask("99.999.999/9999-99",{placeholder:" "});
	$("input[name='matricula']").inputmask("99999999-9",{placeholder:" "});
	$(".numeric").each(function(){
		$(this).inputmask({"mask":"9","repeat":$(this).attr("size"),"greedy":false});
	});
	$(".datefield input").inputmask("d/m/y");
	$('.cep').inputmask("99999-999");
	$(".currency").each(function(){
		$(this).inputmask("999999,99",{numericInput:true,placeholder:" "});
	});
	$("input[name='placa']").inputmask("aaa-9999",{placeholder:" "});
});