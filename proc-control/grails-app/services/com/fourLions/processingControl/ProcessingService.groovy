package com.fourLions.processingControl

import grails.core.GrailsApplication

class ProcessingService {

    GrailsApplication grailsApplication

    def runProcessing(Processing proc, Date date) {
        if (proc.service) {
            String stacktrace
            ProcessingExecution procExec
            Date start, end
            Exception error
            ProcessingExecution.withTransaction { status ->
                log.info "Iniciando Proc #$proc.id - ($proc.service) ... "
                ExecutableProcessing process = grailsApplication.mainContext.getBean(proc.service)
                start = new Date()
                procExec = new ProcessingExecution(processing: proc, startTime: start)
                procExec.save(flush: true)
                try {
                    process.execute(date)
                } catch (e) {
                    error = e
                    stacktrace = stackTraceToString(e)
                    log.error "Erro durante processamento ($proc.service). Verifique registro de Processamento para mais detalhes."
                    status.setRollbackOnly()
                } finally {
                    end = new Date()
                }
                procExec.endTime = end
                procExec.executionStatus = ExecutionStatus.FINISHED_OK
                procExec.save(flush: true)
            }
            if (error) {
                ProcessingExecution.withTransaction {
                    procExec = new ProcessingExecution()
                    procExec.with {
                        processing = proc
                        startTime = start
                        details = stackTraceToString(error)
                        executionStatus = ExecutionStatus.FINISHED_ERROR
                        endTime = end
                    }
                    procExec.save(flush: true)
                }
            }
            log.info "Proc #$proc.id finalizado"
        } else {
            log.warn "Processamento #$proc.id sem SERVICE definido em configuração!!!"
        }
    }

    private String stackTraceToString(t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        sw.toString();
    }

    def run(Date date) {
        BatchProcessing.findAllByActive(true).each { batch ->
            if (batch.runNow(date)) {
                batch.processings.sort { it.order }.each { proc ->
                    runProcessing(proc, date.clearTime())
                }
            }
        }
        def procList = Processing.executeQuery("""select pr from Processing pr, ExecutionSchedule es
                                                    where pr.batch is null and es.processing = pr
                                                    and pr.active = true""")
        procList.each { proc ->
            println "Proc: ${proc.name}"

            if (proc.executionSchedule && proc.runNow(date))
                runProcessing(proc, date.clearTime())
        }
    }
}
