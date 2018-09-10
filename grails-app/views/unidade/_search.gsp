<%@ page import="com.sysdata.gestaofrota.Unidade" %>


<div class="panel panel-default">
    <div class="panel-heading">Lista de centros de Custo</div>

    <div class="panel-body">

        <g:form controller="${controller}">


            <div class="buttons">
                <g:link controller="unidade" action="create" class="btn btn-default" params="[rhId: rhId]">
                    <span class="glyphicon glyphicon-plus"></span> Criar Centro de Custo
                </g:link>
            </div>
            <br><br>


        <div class="list">
            <table class="table table-striped table-bordered table-hover table-condensed table-default" >
                <thead>
                    <th>Nome</th>
                    <th>Status</th>
                    <th></th>
                </thead>
                <tbody>
                    <g:each in="${Unidade.withCriteria{rh{eq('id',rhId)}}}" var="unidade" >
                        <tr>
                            <td><g:link controller="unidade" action="show" id="${unidade.id}"> ${unidade?.nome}</g:link></td>
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








