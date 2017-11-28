package com.sysdata.gestaofrota


import org.junit.*
import grails.test.mixin.*

@TestFor(ProdutoEstabelecimentoController)
@Mock(ProdutoEstabelecimento)
class ProdutoEstabelecimentoControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/produtoEstabelecimento/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.produtoEstabelecimentoInstanceList.size() == 0
        assert model.produtoEstabelecimentoInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.produtoEstabelecimentoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.produtoEstabelecimentoInstance != null
        assert view == '/produtoEstabelecimento/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/produtoEstabelecimento/show/1'
        assert controller.flash.message != null
        assert ProdutoEstabelecimento.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/produtoEstabelecimento/list'


        populateValidParams(params)
        def produtoEstabelecimento = new ProdutoEstabelecimento(params)

        assert produtoEstabelecimento.save() != null

        params.id = produtoEstabelecimento.id

        def model = controller.show()

        assert model.produtoEstabelecimentoInstance == produtoEstabelecimento
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/produtoEstabelecimento/list'


        populateValidParams(params)
        def produtoEstabelecimento = new ProdutoEstabelecimento(params)

        assert produtoEstabelecimento.save() != null

        params.id = produtoEstabelecimento.id

        def model = controller.edit()

        assert model.produtoEstabelecimentoInstance == produtoEstabelecimento
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/produtoEstabelecimento/list'

        response.reset()


        populateValidParams(params)
        def produtoEstabelecimento = new ProdutoEstabelecimento(params)

        assert produtoEstabelecimento.save() != null

        // test invalid parameters in update
        params.id = produtoEstabelecimento.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/produtoEstabelecimento/edit"
        assert model.produtoEstabelecimentoInstance != null

        produtoEstabelecimento.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/produtoEstabelecimento/show/$produtoEstabelecimento.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        produtoEstabelecimento.clearErrors()

        populateValidParams(params)
        params.id = produtoEstabelecimento.id
        params.version = -1
        controller.update()

        assert view == "/produtoEstabelecimento/edit"
        assert model.produtoEstabelecimentoInstance != null
        assert model.produtoEstabelecimentoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/produtoEstabelecimento/list'

        response.reset()

        populateValidParams(params)
        def produtoEstabelecimento = new ProdutoEstabelecimento(params)

        assert produtoEstabelecimento.save() != null
        assert ProdutoEstabelecimento.count() == 1

        params.id = produtoEstabelecimento.id

        controller.delete()

        assert ProdutoEstabelecimento.count() == 0
        assert ProdutoEstabelecimento.get(produtoEstabelecimento.id) == null
        assert response.redirectedUrl == '/produtoEstabelecimento/list'
    }
}
