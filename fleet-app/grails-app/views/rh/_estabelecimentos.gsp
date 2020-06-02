<g:hiddenField name="rhId" value="${rhInstance?.id}"/>

<div class="buttons">
	<span class="button">
		<input type="button" id="btnSaveEst" class="btn btn-default" value="Salvar"/>

	</span>
</div>
<br><br>

%{--
<input type="checkbox" class="enable" id="selFull"> Selecionar todos
--}%

<table id="estTable" class="table table-striped table-bordered table-hover table-condensed" style="font-size: 12px">
	<thead>
		<th><input type="checkbox" class="enable" id="selAll"></th>
        <th>CNPJ</th>
        <th>Razão Social</th>
        <th>Nome Fantasia</th>
	</thead>
</table>


<script type="text/javascript">

	$(document).ready(function(){

		$("#estTable").DataTable({

            "ajax":{
                "url":"${createLink(controller:'postoCombustivel',action:'listAllJSON')}",
				"data":{prgId:+$("#rhId").val()},
                "dataSrc":"results",
            },
            "columns":[
                {"data":"sel"},
                {"data":"cnpj"},
                {"data":"razao"},
                {"data":"nomeFantasia"}
            ]
        });


		$("#btnSaveEst").click(function(){
			var estabs = document.getElementsByClassName('target');
			console.log(estabs);
			$.ajax({
				type:'POST',
				url:"${createLink(action:'saveEstabs')}",
				data:{prgId:+$("#rhId").val()},

				success:function(o){
					if(o.type=="error")
						alert(o.message);
					else
						alert("Empresas lojistas salvas")
				},
				statusCode:{
					404:function(){
						alert("Falhar ao tentar conexão ao servidor");
					}
				}
			})
		});


		$("#selAll").click(function(){
            if($(this).is(":checked")){

				$("#estTable > tbody > tr").each(function(ind){
					var targets = document.getElementsByClassName('target');
						for (i = 0; i < targets.length; i++) {
							targets[i].checked = true;
						}
                });
            } else {
				$("#estTable > tbody > tr").each(function(ind){
					var targets = document.getElementsByClassName('target');
					for (i = 0; i < targets.length; i++) {
						targets[i].checked = false;
					}
				});
			}
        });

		$("#selFull").click(function(){
			if($(this).is(":checked")){

				$("#estTable > tbody > tr").each(function(ind){
					var targets = document.getElementsByClassName('target');
					for (i = 0; i < targets.length; i++) {
						targets[i].checked = true;
					}
				});
			} else {
				$("#estTable > tbody > tr").each(function(ind){
					var targets = document.getElementsByClassName('target');
					for (i = 0; i < targets.length; i++) {
						targets[i].checked = false;
					}
				});
			}
		});

		document.body.addEventListener('DOMSubtreeModified', function(event) {
			var targets = document.getElementsByClassName('target');

			if($("#selAll").prop("checked") == "checked"){
				for (i = 0; i < targets.length; i++) {
					targets[i].checked = true;
				}
			}

			/**
			 * If I have 40 target elements, this will be called 40 times :/ showing first bunch of zeros then finally number will reach to 40
			 */
		});

		/**
		 * So this would be ideally (called only once) but this always shows empty array and 0
		 */
		document.addEventListener('DOMContentLoaded', function(event) {
			var targets = document.getElementsByClassName('target');
			console.log(targets); // always shows []
			console.log(targets.length); // always shows 0

			//while I can play with those target selectors in Chrome Dev Tools
		});

	});

</script>
