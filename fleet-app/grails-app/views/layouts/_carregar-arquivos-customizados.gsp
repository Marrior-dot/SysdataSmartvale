<style type="text/css">
    :root {
        --cor-primaria: ${projeto.corPrimaria};
        --cor-secundaria: ${projeto.corSecundaria};
    }
</style>

<title><g:layoutTitle default="${projeto?.nome}"/></title>
<link rel="shortcut icon" href="${assetPath(src: 'projetos/' + projeto.pasta + '/icon.png')}" type="image/x-icon"/>


<asset:stylesheet src="bootstrap/bootstrap.min.css"/>
<asset:stylesheet src="cores.css"/>
<asset:stylesheet src="estilo.css"/>
