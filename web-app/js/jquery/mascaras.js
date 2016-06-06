/**
 * Created by hyago on 06/06/16.
 */
$(document).ready(function () {
	configInputMasks();
});

function configInputMasks() {
	$('.date').mask("99/99/9999", {placeholder: '__/__/____'});
	$('.cpf').mask("999.999.999-99", {placeholder: '___.___.___-__'});
	$('.cartao').mask("9999.9999.9999.9999", {placeholder: '____.____.____.____'});
	$('.cep').mask("99999-999", {placeholder: '_____-___'});
	$('.phone-number').mask("99999-9999");
	$('.home-number').mask("9999-9999");
	$('.ddd').mask("99");
	$('.phone').mask("(99) 99999-9999", {placeholder: '(__) _____-____'});
	$(".cnpj").mask("99.999.999/9999-99", {placeholder: '__.___.___/____-__'});
	$(".cnpj-filial").mask("9999-99", {placeholder: '____-__'});
	$(".matricula").mask("99999999-9",{placeholder:"________-_"});
	$(".cvv").mask("999");
	$(".agencia").mask("9999999999");
	$(".conta").mask("999999999999");
	$(".number").mask("9999999999999999999");
	$(".decimalmask").mask("99.9");
	$('.decimal').mask('#.##0,00', {reverse: true});
	$('.percentual').mask('000.00 %');
	$('.money').maskMoney({prefix: 'R$ ', decimal: ',', thousands: '.', affixesStay: false});
	$(".placa").mask("aaa-9999",{placeholder:"___-____"});

	$('.only-numbers').keyup(function () {
		var n = this.value.replace(/[^0-9\.]/g, '');
		if (this.value != n) this.value = n;
	});
}