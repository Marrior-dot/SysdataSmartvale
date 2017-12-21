package com.sysdata.gestaofrota


import org.junit.*
import grails.test.mixin.*

@TestFor(FechamentoController)
@Mock(Fechamento)
class FechamentoControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/fechamento/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.fechamentoInstanceList.size() == 0
        assert model.fechamentoInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.fechamentoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.fechamentoInstance != null
        assert view == '/fechamento/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/fechamento/show/1'
        assert controller.flash.message != null
        assert Fechamento.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/fechamento/list'


        populateValidParams(params)
        def fechamento = new Fechamento(params)

        assert fechamento.save() != null

        params.id = fechamento.id

        def model = controller.show()

        assert model.fechamentoInstance == fechamento
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/fechamento/list'


        populateValidParams(params)
        def fechamento = new Fechamento(params)

        assert fechamento.save() != null

        params.id = fechamento.id

        def model = controller.edit()

        assert model.fechamentoInstance == fechamento
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/fechamento/list'

        response.reset()


        populateValidParams(params)
        def fechamento = new Fechamento(params)

        assert fechamento.save() != null

        // test invalid parameters in update
        params.id = fechamento.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/fechamento/edit"
        assert model.fechamentoInstance != null

        fechamento.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/fechamento/show/$fechamento.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        fechamento.clearErrors()

        populateValidParams(params)
        params.id = fechamento.id
        params.version = -1
        controller.update()

        assert view == "/fechamento/edit"
        assert model.fechamentoInstance != null
        assert model.fechamentoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/fechamento/list'

        response.reset()

        populateValidParams(params)
        def fechamento = new Fechamento(params)

        assert fechamento.save() != null
        assert Fechamento.count() == 1

        params.id = fechamento.id

        controller.delete()

        assert Fechamento.count() == 0
        assert Fechamento.get(fechamento.id) == null
        assert response.redirectedUrl == '/fechamento/list'
    }
}
