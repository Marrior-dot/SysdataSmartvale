$(function(){
	$(".uppercase input").focusout(function(){
		$(this).val($(this).val().toUpperCase());
	});
})