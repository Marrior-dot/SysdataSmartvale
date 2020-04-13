<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout-restrito" />
        <title>Pesquisar por Cartão</title>
    </head>
    <body>

    <div class="body">
        <br/>
        <g:if test="${flash.message}">
            <div class="alert alert-info" role="alert">${flash.message}</div>
        </g:if>
        <g:if test="${flash.errors}">
            <div class="alert alert-danger" role="alert">
                <g:each var="err" in="${flash.errors}">
                    <strong>${err}</strong>
                </g:each>
            </div>
        </g:if>
        <div class="panel panel-default" style="width: 40%">
            <div class="panel-heading">

                <g:if test="${goTo == 'unlockNewCard'}">
                    <h4>Desbloqueio de Cartão</h4>
                </g:if>
                <g:if test="${goTo == 'cancelCard'}">
                    <h4>Cancelamento de Cartão</h4>
                </g:if>


            </div>
            <div class="panel-body">
                <div class="buttons">
                    <a type="button" class="btn btn-default" href="${createLink(uri: '/')}">
                        <i class="glyphicon glyphicon-home"></i>
                        <g:message code="default.home.label"/>
                    </a>
                </div>
                <br/>

                <g:form method="post" action="${act}">
                    <g:hiddenField name="goTo" value="${goTo}"/>

                    <bs:formField id="cartao" name="cartao" label="Cartão" class="only-numbers" maxlength="19" />

                    <button class="btn btn-default" type="submit" name="${act}">
                        <i class="glyphicon glyphicon-search"></i>
                        Pesquisar
                    </button>
                </g:form>

            </div>
        </div>
    </div>
    </body>
</html>


