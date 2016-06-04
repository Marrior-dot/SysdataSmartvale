<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'carro.png')}" type="image/x-icon" />
        <g:javascript library="jquery" plugin="jquery"/>
		<gui:resources components="['tabView','dataTable','dialog','datePicker','autoComplete']"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'frota.css')}" />

		<script type="text/javascript" src="${resource(dir:'js/jquery/jquery.inputmask',file:'jquery.inputmask.js') }" ></script>       
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'enableFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'maskFields.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js/jquery',file:'uppercase.js') }"></script>
        <script type="text/javascript" src="${resource(dir:'js',file:'messageWindow.js') }"></script>
        
        <script type="text/javascript" src="${resource(dir:'js',file:'util.js') }"></script>
        
        <g:layoutHead />
        
    </head>
    
    <script type="text/javascript">
		$(window).resize(function(){
			expandHeaderX($("#frotaBanner img"));
		});

		$(window).load(function(){
			$(window).resize();
		});    	
	</script>
    
    
    <body class="yui-skin-sam">
    
    	<gui:dialog
			id="messageDialog"
			title="Gestão de Frota"
			draggable="false"
			width="500px"
			update="messageForm"
			fixedcenter="true"
			close="false"
			modal="true">
			<div id="messageForm"></div>			
		</gui:dialog>
    
    	<div id="page">
	    	<div id="header">
	    		<sec:ifLoggedIn>
	    			<span class="close">Oi, <sec:username /> ( <a id="close" href="${createLink(controller:'logout')}">Sair</a> | <a id="psw" href="${createLink(controller:'user',action:"editPassword")}">Trocar Senha</a> )</span>
	    		</sec:ifLoggedIn>
	    		
	    		<div id="frotaBanner">
		    		<img alt="" src="${resource(dir:'images',file:'frota_banner.png') }">
	    		</div>
	    		
	    	</div>
	    	
	    
	        <div id="spinner" class="spinner" style="display:none;">
	            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
	        </div>
	        <div id="middle">
	        	<div id="sidebar">
	        	</div>
	        	<div id="main">
			        <g:layoutBody />
	        	</div>
	        </div>
	        <div id="footer">
				<p id="copyright">Copyright &copy; 2012-2016 Sysdata Tecnologia.	Todos os direitos reservados</p>
			</div>
    	</div>
    
    </body>
</html>