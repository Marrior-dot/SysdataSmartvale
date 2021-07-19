package com.fourLions.processingControl

enum ExecutionFrequency {

    DAILY("Diária"),
    WEEKLY("Semanal"),
    MONTHLY("Mensal"),
    ONCE("Única vez")

    String name

    ExecutionFrequency(name) {
        this.name = name
    }

}