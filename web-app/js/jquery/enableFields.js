jQuery(function(){
	$(document).ready(function(){
		if($("#action").val()=="visualizando"){
			$("div.enter input").each(function(){
				$(this).attr("readonly","true")
			});
			$(".disable input").each(function(){
				$(this).attr("disabled","true")
			});
			
		}
	})	
})