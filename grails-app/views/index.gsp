<html>
    <head>
        <title>Bem vindo ao Sistema Gestão de Frota</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">


	            
	    .clear{
			clear:both;
		}

		#servicos{
		   margin-left:100px;	
		   margin-top:100px;
		   margin-bottom:100px;
		}

        .servico{
        	width:150px;
        	float:left;
        }
        
       .servico h2{
       		text-align:center;
       }
        
        </style>
        
        
        
    </head>
    <body>
      
        	<div id="user">
        		<img src="${resource(dir:'images',file:'usuario.png')}" alt="Usuário Cenpros" border="0"/>
        		<span style="font: 20px sans-serif ;color: #4682B4 " >Bem vindo(a) ao Gestão de Frota</span>
        	</div>
			
			<hr>      
            
            <div id="servicos">
	            <div class="servico">
	            	<a href="${createLink(controller:'login',action:'menu')}?servico=cadastros">
	            		<img alt="Cadastros" src="${resource(dir:"images",file:"cadastro.png") }">
	            	</a>
					<h2>Cadastros</h2>						            	
	            </div>
	            
	            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_RH">
	            <div class="servico">
	            	<a href="${createLink(controller:'login',action:'menu')}?servico=financeiro">
	            		<img alt="Financeiro" src="${resource(dir:"images",file:"financeiro.png") }">
	            	</a>
	            	<h2>Financeiro</h2>
	            </div>
	            </sec:ifAnyGranted>
	            
	            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC,ROLE_ESTAB,ROLE_RH">
		            <div class="servico">
						<a href="${createLink(controller:'login',action:'menu')}?servico=relatorios">
		            		<img alt="Relatorios" src="${resource(dir:"images",file:"relatorio.png") }">
		            	</a>
		            	<h2>Relatórios</h2>
		            </div>
		        </sec:ifAnyGranted>
		            
		        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_PROC">
		            <div class="servico">
						<a href="${createLink(controller:'login',action:'menu')}?servico=ca">
		            		<img alt="Central de Atendimento" src="${resource(dir:"images",file:"ca.png") }">
		            	</a>
		            	<h2>Central de Atendimento</h2>
		            </div>
	            
	            </sec:ifAnyGranted>
            
	            
	                        
      			<div class="clear"></div>   
	         </div>   
	                     
    </body>
</html>
