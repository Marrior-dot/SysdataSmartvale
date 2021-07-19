package com.fourLions.processingControl

enum ExecutionStatus {

    CREATED("Criado"),
    EXECUTING("Executando"),
    FINISHED_OK("Finalizado OK"),
    FINISHED_ERROR("Finalizado com Erros")

    String name

    ExecutionStatus(name) {
        this.name = name
    }

}