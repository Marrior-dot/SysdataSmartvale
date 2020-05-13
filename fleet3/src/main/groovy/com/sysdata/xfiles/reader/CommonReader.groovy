package com.sysdata.xfiles.reader

import com.sysdata.xfiles.FieldDataType
import com.sysdata.xfiles.FileProcessorException
import com.sysdata.xfiles.SpecRecord

import java.text.SimpleDateFormat


abstract class CommonReader {

    File file

    List<SpecRecord> specRecordList

    // Mapa de variáveis globais ao processamento do arquivo
    Map vars = [:]

    // Contador geral de linhas
    Integer counter = 0

    // Mapa de valores de campos por linha
    Map cols = [:]

    // Spec de Registro por linha
    SpecRecord specRecord

    // Linha inicial de leitura/escrite
    Integer startLine = 0

    // Se tem identificador de registro. Caso contrário, apenas uma spec de registro é considerada
    Boolean hasRecordIdentifier = true

    // Event Listener - EOF
    Closure eofListener

    abstract SpecRecord findSpecRecord(String line)

    abstract Map processLine(String line, Closure clos)

    def eachLine(String line, Closure clos) {

        // Limpa mapas de variáveis por linha
        this.cols.clear()

        this.vars.errors.clear()

        // Pula linhas até chegar na start line / Incrementa de 1 o contador de linha
        if (this.counter++ < this.startLine) return

        // Encontra qual Spec de Registra irá tratar a linha
        this.specRecord = findSpecRecord(line)
        if (! this.specRecord) throw new FileProcessorException("L:($counter) -> Nenhuma spec encontrada para tratar a linha $counter")

        def lineErrors = processLine(line, clos)

        // Passa contador de linhas para mapa de variavéis globais
        this.vars.counter = this.counter

        // Só chama handler caso não haja erros de validação
        if (lineErrors.isEmpty()) {
            // Se há uma closure para tratamento de um tipo de registro específico
            if (this.specRecord.handler) this.specRecord.handler(vars, cols)

        } else this.vars.errors[this.counter] = lineErrors

        // Passa erros para mapa de variáveis globais
        //this.vars.errors = this.errors

        // Se há uma closure para tratamento linha a linha
        if (clos) {
            if (clos instanceof Closure) clos(vars)
            else throw new FileProcessorException("Não é uma closure!")
        }

    }

    boolean handleRawValue(String rawValue, Map field, Map fieldsError) {
        def value

        boolean proceed = true
        if (field.required) {
            if (!rawValue) {
                fieldsError[field.name] = "Valor do campo é requerido. Não pode ser nulo!"
                proceed = false
            }
        }

        if (proceed) {
            if (field.datatype) {
                switch (field.datatype) {
                    case FieldDataType.BYTE:
                        value = rawValue as Byte
                        break
                    case FieldDataType.INTEGER:
                        value = rawValue.toInteger()
                        break
                    case FieldDataType.LONG:
                        value = rawValue as Long
                        break
                    case FieldDataType.DATE_TIME:
                        value = new SimpleDateFormat(field.format).parse(rawValue)
                        break
                    case FieldDataType.BIGDECIMAL:
                        value = new BigDecimal(rawValue)
                        break
                    default:
                        throw new FileProcessorException("L:(${this.counter}) - C:${field.name} -> Tipo de campo não tratado ($field.datatype)!")
                }
            } else value = rawValue

            // Chama closure para transformação de valor
            if (field.transform) this.cols[field.name] = field.transform(value)
            else this.cols[field.name] = value

            // Caso haja atributo **value** ( e não for Closure - válido somente pra o Writer) checa se conteúdo do campo bate com valor do atributo
            if (field.value && ! field.value instanceof Closure) {
                if (field.value != this.cols[field.name])
                    fieldsError[field.name] = "Valor do campo (${this.cols[field.name]}) não bate com o valor definido na spec (${field.value})!"
            }
        }
        return proceed
    }

    void processFile(Closure clos) {
        Reader reader = this.file.newReader("UTF-8")

        // Coloca file no mapa global
        this.vars.file = this.file

        // Inicializa registro de erros
        this.vars.errors = [:]

        def line
        while ((line = reader.readLine()) != null)
            eachLine(line, clos)

        // Lança listener EOF se existir
        if (eofListener) eofListener.call(this.vars)

    }

}