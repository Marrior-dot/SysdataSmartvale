package com.sysdata.gestaofrota


import org.junit.*
import grails.test.mixin.*

@TestFor(MaquinaMotorizadaController)
@Mock(MaquinaMotorizada)
class MaquinaMotorizadaControllerTests {


    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/maquinaMotorizada/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.maquinaMotorizadaInstanceList.size() == 0
        assert model.maquinaMotorizadaInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.maquinaMotorizadaInstance != null
    }

    void testSave() {
        controller.save()

        assert model.maquinaMotorizadaInstance != null
        assert view == '/maquinaMotorizada/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/maquinaMotorizada/show/1'
        assert controller.flash.message != null
        assert MaquinaMotorizada.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/maquinaMotorizada/list'


        populateValidParams(params)
        def maquinaMotorizada = new MaquinaMotorizada(params)

        assert maquinaMotorizada.save() != null

        params.id = maquinaMotorizada.id

        def model = controller.show()

        assert model.maquinaMotorizadaInstance == maquinaMotorizada
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/maquinaMotorizada/list'


        populateValidParams(params)
        def maquinaMotorizada = new MaquinaMotorizada(params)

        assert maquinaMotorizada.save() != null

        params.id = maquinaMotorizada.id

        def model = controller.edit()

        assert model.maquinaMotorizadaInstance == maquinaMotorizada
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/maquinaMotorizada/list'

        response.reset()


        populateValidParams(params)
        def maquinaMotorizada = new MaquinaMotorizada(params)

        assert maquinaMotorizada.save() != null

        // test invalid parameters in update
        params.id = maquinaMotorizada.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/maquinaMotorizada/edit"
        assert model.maquinaMotorizadaInstance != null

        maquinaMotorizada.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/maquinaMotorizada/show/$maquinaMotorizada.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        maquinaMotorizada.clearErrors()

        populateValidParams(params)
        params.id = maquinaMotorizada.id
        params.version = -1
        controller.update()

        assert view == "/maquinaMotorizada/edit"
        assert model.maquinaMotorizadaInstance != null
        assert model.maquinaMotorizadaInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/maquinaMotorizada/list'

        response.reset()

        populateValidParams(params)
        def maquinaMotorizada = new MaquinaMotorizada(params)

        assert maquinaMotorizada.save() != null
        assert MaquinaMotorizada.count() == 1

        params.id = maquinaMotorizada.id

        controller.delete()

        assert MaquinaMotorizada.count() == 0
        assert MaquinaMotorizada.get(maquinaMotorizada.id) == null
        assert response.redirectedUrl == '/maquinaMotorizada/list'
    }
}
