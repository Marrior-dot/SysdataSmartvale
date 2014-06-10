function openMessage(type,msg){
	
	var url=document.location.href;
	var barra=url.indexOf("/",7);

	url=url.substring(barra);
	
	barra=url.indexOf("/",1);
	
	url=url.substring(0,barra);
	
	$.ajax({
		type:'POST',
		url:url+"/message",
		success:function(data){
			$("#messageForm").html(data);
			if(type=="ok"){
				$("div.ok").html(msg);
				$("div.ok").attr('style','display:block');
				$("div.error").attr('style','display:none');
				$("div.errorsList").attr('style','display:none');
			}else if(type=="err"){
				$("div.error").html(msg);
				$("div.error").attr('style','display:block');
				$("div.ok").attr('style','display:none');
				$("div.errorsList").attr('style','display:none');
			}else if(type=="errLst"){
				$("div.errorsList").html(msg);
				$("div.errorsList").attr('style','display:block');
				$("div.error").attr('style','display:none');
				$("div.ok").attr('style','display:none');
			}
			GRAILSUI.messageDialog.show();
		},
		statusCode:{
			404:function(){
				alert("Falha ao abrir mensagem");
			}
		}
	});
}

function showMessage(msg){
	openMessage("ok",msg);
}

function showError(err){
	openMessage("err",err);
}

errorList="";

function addErrorList(err){
	errorList+="<li>"+err+"</li>"
}

function showErrorList(){
	openMessage("errLst","<ul>"+errorList+"</ul>");
	errorList="";
}
