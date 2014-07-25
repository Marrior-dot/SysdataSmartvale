$(document).ready(function(){

	$("#role").focus(function(){
		
		var combo=$("#owner")
		var text=$("#owner option:selected").text()
		var index=text.indexOf(" ")
		var participante=text.substring(index+3,text.length)
		var classe=text.substring(0, index)
		var parametro = {
				nome : participante,
				classe : classe
			}
		
		var options;
		
		var combo1=$("#role")
		var text1=$("#role option:selected").text()
		
		var x=$.get("listRoles", parametro, function(role, status){
			if(role.length){
				for(var i=0;i<role.length;i++){
					options += '<option value="' + role[i].id + '">' + role[i].authority + '</option>';
				}
				
				$("#role").html(options);
			}else{
				options += '<option value="' + role.id + '">' + role.authority + '</option>';
				$("#role").html(options);
			}		
		});
		$("#role option:selected")=text1
		
	});
		
});