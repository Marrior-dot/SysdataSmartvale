package com.sysdata.gestaofrota


import org.junit.*
import grails.test.mixin.*

@TestFor(CartaoController)
@Mock(Cartao)
class CartaoControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/cartao/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.cartaoInstanceList.size() == 0
        assert model.cartaoInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.cartaoInstance != null
    }

    void testSave() {
        controller.save()

        assert model.cartaoInstance != null
        assert view == '/cartao/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/cartao/show/1'
        assert controller.flash.message != null
        assert Cartao.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/cartao/list'


        populateValidParams(params)
        def cartao = new Cartao(params)

        assert cartao.save() != null

        params.id = cartao.id

        def model = controller.show()

        assert model.cartaoInstance == cartao
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/cartao/list'


        populateValidParams(params)
        def cartao = new Cartao(params)

        assert cartao.save() != null

        params.id = cartao.id

        def model = controller.edit()

        assert model.cartaoInstance == cartao
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/cartao/list'

        response.reset()


        populateValidParams(params)
        def cartao = new Cartao(params)

        assert cartao.save() != null

        // test invalid parameters in update
        params.id = cartao.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/cartao/edit"
        assert model.cartaoInstance != null

        cartao.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/cartao/show/$cartao.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        cartao.clearErrors()

        populateValidParams(params)
        params.id = cartao.id
        params.version = -1
        controller.update()

        assert view == "/cartao/edit"
        assert model.cartaoInstance != null
        assert model.cartaoInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/cartao/list'

        response.reset()

        populateValidParams(params)
        def cartao = new Cartao(params)

        assert cartao.save() != null
        assert Cartao.count() == 1

        params.id = cartao.id

        controller.delete()

        assert Cartao.count() == 0
        assert Cartao.get(cartao.id) == null
        assert response.redirectedUrl == '/cartao/list'
    }
}
