/**
 * Created by hyago on 06/06/16.
 */
$(document).ready(function () {
	configDatepicker();
	configInputMasks();
});

function configDatepicker() {
	$.fn.datepicker.dates['pt-BR'] = {
		days: ["Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado"],
		daysShort: ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
		daysMin: ["Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"],
		months: ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"],
		monthsShort: ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Otu", "Nov", "Dez"],
		today: "Hoje",
		clear: "Limpar",
		format: "dd/mm/yyyy",
		titleFormat: "MM yyyy",
		weekStart: 0
	};
	$('.datepicker').datepicker({language: 'pt-BR'});
}

function configInputMasks() {
	$('.date').mask("99/99/9999", {placeholder: '__/__/____'});
	$('.cpf').mask("999.999.999-99", {placeholder: '___.___.___-__'});
	$('.cartao').mask("9999.9999.9999.9999", {placeholder: '____.____.____.____'});
	$('.cep').mask("99999-999", {placeholder: '_____-___'});
	$('.cell-phone').mask("99999-9999");
	$('.home-phone').mask("9999-9999");
	$('.ddd').mask("99");
	$('.phone').mask("(99) 99999-9999", {placeholder: '(__) _____-____'});
	$(".cnpj").mask("99.999.999/9999-99", {placeholder: '__.___.___/____-__'});
	$(".cnpj-filial").mask("9999-99", {placeholder: '____-__'});
	$(".matricula").mask("99999999-9", {placeholder: "________-_"});
	$(".cvv").mask("999");
	$(".agencia").mask("9999999999");
	$(".conta").mask("999999999999");
	$(".number").mask("9999999999999999999");
	$(".decimalmask").mask("99.9");
	$('.decimal').mask('#.##0,00', {reverse: true});
	$('.percentual').mask('00,00', {reverse: true});
	$('.money').maskMoney({prefix: 'R$ ', decimal: ',', thousands: '.', affixesStay: false});
	$(".placa").mask("AAA-9A99", {placeholder: "___-____"});
	$(".anoFabricacao").mask("9999");
	$(".maxValor").mask("99999");
	$(".hodometro").mask("999999999");

	$('.only-numbers').keyup(function () {
		var n = this.value.replace(/[^0-9\.]/g, '');
		if (this.value != n) this.value = n;
	});

	$("input.uppercase").keyup(function () {
		$(this).val($(this).val().toUpperCase());
	});
}