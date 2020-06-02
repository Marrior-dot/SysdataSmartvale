package com.fourLions.processingControl

/*
  Qualquer processamento para ser executado deve ser um service que implemente esta interface
 */
interface ExecutableProcessing {
    def execute(Date date)
}