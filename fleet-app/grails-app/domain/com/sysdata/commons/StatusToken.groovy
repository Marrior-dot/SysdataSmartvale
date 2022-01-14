package com.sysdata.commons

enum StatusToken {

    CREATED("Criado"),
    USED("Utilizado", "Requisição de nova senha já utilizada!"),
    EXPIRED("Expirado", "Requisição de nova senha expirada!"),
    INVALID("Inválido", "Requisição de nova senha inválida!")

    String name
    String userMessage

    StatusToken(name, userMessage = "") {
        this.name = name
        this.userMessage = userMessage
    }
}