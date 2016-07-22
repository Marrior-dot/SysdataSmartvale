//Configuração padrão para todos os DataTables
$.extend($.fn.dataTable.defaults, {

    "lengthChange": false,

    language: {
        search: "Filtro:",
        loadingRecords: "Carregando...",

        paginate: {
            first: "Primeiro",
            previous: "Anterior",
            next: "Pr&oacute;ximo",
            last: "&Uacute;ltimo"
        }
    }

});


function expandHeaderX(logoHeader) {

    /* Altura e largura da janela do browser */
    var winWidth = $(window).width();

    var imgHeight = logoHeader.height();
    var imgWidth = logoHeader.width();

    logoHeader
        .removeAttr("width")
        .removeAttr("height")
        .css({width: "", height: ""})

    /* Largura alvo: largura da janela */
    var tgtWidth = winWidth;
//	/* Redimensiona altura na mesma proporção da imagem original */
//	var tgtHeight=(tgtWidth*imgHeight)/imgWidth;
//	
//	tgtHeight=Math.round(tgtHeight);
//	
    $("#frotaBanner img").height(imgHeight);
    $("#frotaBanner img").width(tgtWidth);

}


function filtrarEntidade(dataTable, filtro) {

    dataTable.customQueryString = filtro;
    dataTable.loadingDialog.show();
    dataTable.cleanup();

    var sortedBy = dataTable.get('sortedBy');
    var newState = {
        startIndex: 0,
        sorting: {
            key: sortedBy.key,
            dir: ((sortedBy.dir == YAHOO.widget.DataTable.CLASS_DESC) ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC)
        },
        pagination: {
            recordOffset: 0,
            rowsPerPage: dataTable.get("paginator").getRowsPerPage()
        }
    };

    var oCallback = {
        success: dataTable.onDataReturnInitializeTable,
        failure: dataTable.onDataReturnInitializeTable,
        scope: dataTable,
        argument: newState
    };

    dataTable.getDataSource().sendRequest(dataTable.buildQueryString(newState), oCallback);

}


