package com.sysdata.commons

enum StatusToken {

    CREATED("Criado"),
    USED("Utilizado"),
    EXPIRED("Expirado"),
    CANCELED("Cancelado")

    String name

    StatusToken(name) {
        this.name = name
    }
}