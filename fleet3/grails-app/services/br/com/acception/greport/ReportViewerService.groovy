package br.com.acception.greport

import java.text.SimpleDateFormat

class ReportViewerService {

    def grailsApplication
    def sessionFactory

    private def castReportParams(params, reportInstance) {

        def castValues = [:]

        reportInstance.parameters.each { paramInstance ->
            if (params.containsKey(paramInstance.name)) {
                def rawValue = params[paramInstance.name]

                if (rawValue == "" && paramInstance.mandatory)
                    throw new RuntimeException("Parâmetro ${paramInstance.name} é obrigatório")

                try {
                    switch (paramInstance.dataType) {
                        case DataType.INTEGER:
                            castValues[paramInstance.name] = rawValue as int
                            break;
                        case DataType.LONG:
                            castValues[paramInstance.name] = rawValue as long
                            break;
                        case DataType.FLOAT:
                            castValues[paramInstance.name] = rawValue as float
                            break;
                        case DataType.DOUBLE:
                            castValues[paramInstance.name] = rawValue as double
                            break;
                        case DataType.DATE:
                            castValues[paramInstance.name] = new SimpleDateFormat("dd/MM/yyyy").parse(rawValue)
                            break;
                        case DataType.STRING:
                            castValues[paramInstance.name] = rawValue
                            break;

                        default:
                            throw new RuntimeException("Parâmetro ${paramInstance.name} definido com tipo invalido!")
                    }

                } catch (NumberFormatException e) {
                    throw new RuntimeException("Relatorio:${reportInstance.name} - Erro ao converter parametro da consulta [${paramInstance.name}] para [${paramInstance.dataType}]")
                }
            }
        }

        castValues

    }

    private String formatValue(fieldInstance, rawValue) {
        String formatValue
        switch (fieldInstance.dataType) {
            case DataType.FLOAT:
                formatValue = String.format("%.2f", rawValue as float)
                break;
            case DataType.DOUBLE:
                formatValue = String.format("%.2f", rawValue as double)
                break;
            case DataType.DATE:
                formatValue = new SimpleDateFormat("dd/MM/yyyy").format(rawValue)
                break;
            case DataType.TIMESTAMP:
                formatValue = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rawValue)
                break;
            default:
                formatValue = rawValue
                break;
        }
        formatValue
    }

    private boolean isLastPage(params, total) {
        def offset = params.offset ? params.offset as int : 0
        def max = params.max as int

        (max + offset) >= total

    }

    private def calculateTotalizers(reportInstance, params, total, castParams) {

        def tots = [:]

        def totalizers = reportInstance.fields.findAll { it.totalizer }

        totalizers.each {
            tots[it.name] = [label: it.label, sum: 0]
        }


        if (totalizers && isLastPage(params, total)) {

            def resultTot

            switch (reportInstance.queryType) {
                case QueryType.SQL:
                    break;

                case QueryType.HQL:
                    resultTot = Report.executeQuery(reportInstance.query, castParams)
                    break;

                case QueryType.CRITERIA:

                    break;

                default:
                    break;
            }

            resultTot.each { rs ->
                if (grailsApplication.isDomainClass(rs.class)) {

                    totalizers.each { totField ->
                        if (rs.properties.containsKey(totField.name)) {

                            tots[totField.name] = (tots[totField.name] ?: 0) + rs.properties[totField.name]
                        } else
                            throw new RuntimeException("Relatorio:${reportInstance.name} - Campo totalizador [${totField.name}] nao retornado na consulta")
                    }


                } else if (rs instanceof Object[]) {

//				/* Localiza metainfo de fields pela ordem de retorno na consulta */
                    rs.eachWithIndex { val, i ->

                        def totField = totalizers.find { it.order == (i + 1) }
                        if (totField) {
                            def m = tots[totField.name]
                            m.sum += rs[i]
                        }
//							(tots[totField.name]?:0)+rs[i]
//						else
//							throw new RuntimeException("Relatorio:${reportInstance.name} - Campo totalizador de ordem ${i+1} nao localizado na configuracao")
                    }

                } else {
                    def totField = totalizers.find { it.order == 1 }
                    if (totField)
                        tots[totField.name] = (tots[totField.name] ?: 0) + rs.properties[totField.name]
                    else
                        throw new RuntimeException("Relatorio:${reportInstance.name} - Nao ha configuracao de totalizador de ordem 1")
                }

            }
        }
        tots

    }

    private def mountOutput(reportInstance, resultSet) {
        def rows = []

        /* Prepara colunas de retorno da consulta para relatório */
        resultSet.each { rs ->

            def row = [:]

            if (grailsApplication.isDomainClass(rs.class)) {

                reportInstance.fields.each { fieldInstance ->
                    if (rs.properties.containsKey(fieldInstance.name)) {
                        def rawValue = rs.properties[fieldInstance.name]
                        row[fieldInstance.name] = formatValue(fieldInstance, rawValue)
                    } else
                        throw new RuntimeException("Relatorio:${reportInstance.name} - Campo mapeado [${fieldInstance.name}] nao retornado na consulta")
                }


            } else if (rs instanceof Object[]) {
                /* Localiza metainfo de fields pela ordem de retorno na consulta */
                rs.eachWithIndex { val, i ->
                    def fieldInstance = reportInstance.fields.find { it.order == (i + 1) }
                    if (fieldInstance)
                        row[fieldInstance.name] = formatValue(fieldInstance, val)
                    else
                        throw new RuntimeException("Relatorio:${reportInstance.name} - Campo de retorno de ordem ${i + 1} nao localizado na configuracao")
                }
            } else {
                def fieldInstance = reportInstance.fields.find { it.order == 1 }
                if (fieldInstance)
                    row[fieldInstance.name] = formatValue(fieldInstance, rs)
                else
                    throw new RuntimeException("Relatorio:${reportInstance.name} - Nao ha configuracao de campo de ordem 1")
            }

            if (!row.isEmpty())
                rows << row
        }
        rows
    }

    private String includePaging(sql, max, offset) {

        def sqlPag = sql
        def dbDriver = grailsApplication.config.dataSource.driverClassName

        if (!offset) offset = 0

        if (dbDriver == "org.postgresql.Driver") {
            sqlPag += " limit ${max} offset ${offset}"
        }

        sqlPag
    }


    def list(def params) {
        def reportInstance = Report.get(params?.long('id'))

        def castParams = castReportParams(params, reportInstance)

        def resultCount
        def resultSet

        switch (reportInstance.queryType) {
            case QueryType.SQL:

                def hibSession = sessionFactory.currentSession

                def sqlPaging = includePaging(reportInstance.query, params.max, params.offset)

                def query = hibSession.createSQLQuery(sqlPaging)
                castParams.each { k, v ->
                    query = query.setParameter(k, v)
                }
                resultSet = query.list()

                def queryCount = hibSession.createSQLQuery(reportInstance.countQuery)
                castParams.each { k, v ->
                    queryCount = queryCount.setParameter(k, v)
                }
                resultCount = queryCount.list()

                break;

            case QueryType.HQL:
                resultSet = Report.executeQuery(reportInstance.query, castParams, params)
                resultCount = Report.executeQuery(reportInstance.countQuery, castParams)
                break;

            case QueryType.CRITERIA:
                //TODO: implementing criteria approach
                break;

            default:
                break;
        }

        def rowTotal = calculateTotalizers(reportInstance, params, resultCount[0], castParams)

        def rows = mountOutput(reportInstance, resultSet)

        [rows: rows, rowCount: resultCount[0], rowTotal: rowTotal]
    }
}
