<%@ page import="com.sysdata.gestaofrota.Unidade" %>


<div class="panel panel-default">
    <div class="panel-heading">Lista de RHs</div>

    <div class="panel-body">

        <g:form controller="${controller}">


            <div class="buttons">
                <g:link controller="unidade" action="create" class="btn btn-default">
                    <span class="glyphicon glyphicon-plus"></span> Criar RH
                </g:link>
            </div>
            <br><br>

        <fieldset class="search">
            <input  type="hidden" id="unidId" name="unidId"/>
            <input  type="hidden" name="rhId" value="${rhId}"/>

        <input class="enable" type="radio" name="opcaoUnid" value="1" checked="true">Código</input>
        <input class="enable" type="radio" name="opcaoUnid" value="2">Nome</input>
        <br><br>
        <label>Filtro: <input type="search" class="form-control" id="filtro" name="filtro" value="${filtro}"/></label>
        </fieldset>

        <div class="list">
            <table class="table table-striped table-bordered table-hover table-condensed table-default" >
                <thead>
                    <th>Código</th>
                    <th>Nome</th>
                    <th>Status</th>
                    <th></th>
                </thead>
                <tbody>
                    <g:each in="${Unidade.withCriteria{rh{eq('id',rhId)}}}" var="unidade" >
                        <tr>
                            <td><g:link controller="unidade" action="show" id="${unidade.id}"> ${unidade?.codigo}</g:link></td>
                            <td>${unidade?.nome}</td>
                            <td>${unidade?.status}</td>
                            <td></td>
                        </tr>

                    </g:each>
                </tbody>
            </table>
        </div>


        </div>




    </g:form>

    </div>








