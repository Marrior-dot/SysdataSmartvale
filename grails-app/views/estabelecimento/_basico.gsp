<div class="panel panel-default">
    <div class="panel-heading">
        Dados Basicos
    </div>

    <div class="panel-body">

        <div class="row">
            <div class="col-xs-4">
                <bs:formField id="cnpj" name="cnpj" label="CNPJ"  value="${estabelecimentoInstance?.cnpj}" class="cnpj"/>
            </div>
            <div class="col-xs-6">
                <bs:formField id="nome" name="nome" label="Razao Social"  value="${estabelecimentoInstance?.nome}" />
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6">
                <bs:formField id="nomeFantasia" name="nomeFantasia" label="Nome Fantasia"  value="${estabelecimentoInstance?.nomeFantasia}" />
            </div>

        </div>

        <div class="row">
            <div class="col-xs-4">
                <bs:formField id="inscricaoEstadual" name="inscricaoEstadual" label="Inscrição Estadual" value="${estabelecimentoInstance?.inscricaoEstadual}" class="only-numbers" maxlength="10"/>
            </div>
            <div class="col-xs-4">
                <bs:formField id="inscricaoMunicipal" name="inscricaoMunicipal" label="Inscrição Municipal" value="${estabelecimentoInstance?.inscricaoMunicipal}" class="only-numbers" maxlength="10"/>
            </div>
        </div>


        <g:render template="/endereco/form" model="[enderecoInstance:estabelecimentoInstance?.endereco,endereco:'endereco',legend:'Endereço']"/>

        <g:render template="/telefone/form" model="[telefoneInstance:estabelecimentoInstance?.telefone,telefone:'telefone',legend:'Telefone']"/>

    </div>


</div>




