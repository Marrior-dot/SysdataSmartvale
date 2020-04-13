package com.sysdata.gestaofrota

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.Normalizer
import java.text.SimpleDateFormat

import com.sysdata.gestaofrota.exception.InvalidCurrencyException

class Util {

    static final String ACTION_NEW = "novo"
    static final String ACTION_VIEW = "visualizando"
    static final String ACTION_EDIT = "editando"
    static final String ACTION_FILTER = "filtrando"

    static final Locale LOCAL = new Locale("pt", "BR");

    static final int DIGITOS_SENHA = 4;

    static def getStringMes(int mes) {
        switch (mes) {
            case 1: 'Jan'
                break
            case 2: 'Fev'
                break
            case 3: 'Mar'
                break
            case 4: 'Abr'
                break
            case 5: 'Mai'
                break
            case 6: 'Jun'
                break
            case 7: 'Jul'
                break
            case 8: 'Ago'
                break
            case 9: 'Set'
                break
            case 10: 'Out'
                break
            case 11: 'Nov'
                break
            case 12: 'Dez'
                break
        }
    }

    static def getIntegerMes(String mes) {
        switch (mes) {
            case 'Jan': 1
                break
            case 'Fev': 2
                break
            case 'Mar': 3
                break
            case 'Abr': 4
                break
            case 'Mai': 5
                break
            case 'Jun': 6
                break
            case 'Jul': 7
                break
            case 'Ago': 8
                break
            case 'Set': 9
                break
            case 'Out': 10
                break
            case 'Nov': 11
                break
            case 'Dez': 12
                break
        }
    }

    static def clearMask(masked) {
        masked.replaceAll(/\W/, "")
    }


    static def decimalMode(curr) {
        if (curr instanceof Double)
            curr * 100 as long
        else
            throw new InvalidCurrencyException(message: "Valor $curr não é um Double")
    }


    static def isInteger(val) {
        val ==~ /\d+/
    }

    static def isFloat(val) {
        val ==~ /\d+,\d{2}/
    }

    static String formatDate(def data, String format = 'dd/MM/yyyy') {
        new SimpleDateFormat(format).format(data)
    }

    static String parseDate(String data, String format = 'dd/MM/yyyy') {
        if (data?.length() > 0)
            return new SimpleDateFormat(format).parse(data).format(format)

        return ""
    }

    static def convertToCurrency(value) {
        if (value ==~ /[\d|\.]*,*\d{0,2}/) {
            def convValue = value.contains(".") ? value.replace(".", "") : value
            convValue = convValue.replace(",", ".")
            convValue = convValue as BigDecimal
            convValue
        } else {
            throw new InvalidCurrencyException(message: "Valor $value não é um valor monetário válido!")
        }
    }

    static Double convertToDouble(String value) {
        String convValue = value.contains(".") ? value.replace(".", "") : value
        convValue = convValue.replace(",", ".").replace("R\$ ", "")
        return convValue as BigDecimal
    }

    static def formattedDate(date) {
        date ? new SimpleDateFormat("dd/MM/yyyy").format(date) : ''
    }

    static def formattedDateTime(date) {
        def df = new SimpleDateFormat("dd/MM/yyyy HH:m:ss")
        df.format(date)
    }


    static def formatCurrency(curr) {
        if (!curr) curr = 0.00
        def dfs = new DecimalFormatSymbols(LOCAL)
        dfs.decimalSeparator = ","
        new DecimalFormat("#0.00", dfs).format(curr)
    }

    static def roundCurrency(curr) {
        if (curr instanceof Double) {
            BigDecimal convert = new BigDecimal(curr)
            double arrend = convert.setScale(2, BigDecimal.ROUND_HALF_UP);
            arrend
        }
    }

    static String maskCard(String numero) {
        if (numero?.length() > 6) {
            return numero[0..3] + "****" + numero[(numero.length() - 3)..(numero.length() - 1)]
        } else "<< NÚMERO IMASCARÁVEL >>"
    }

    static BigDecimal toBigDecial(Double valor, int decimalPlace = 2) {
        BigDecimal bd = new BigDecimal(Double.toString(valor));
        bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    }

    static String replaceSpecialChars(String s) {
        if (s) {
            def t = s.replaceAll('ç', 'c')
            t = t.replaceAll('Ç', 'C')
            t = t.replaceAll('á', 'a')
            t = t.replaceAll('à', 'a')
            t = t.replaceAll('â', 'a')
            t = t.replaceAll('ã', 'a')
            t = t.replaceAll('é', 'e')
            t = t.replaceAll('ê', 'e')
            t = t.replaceAll('í', 'i')
            t = t.replaceAll('ó', 'o')
            t = t.replaceAll('õ', 'o')
            t = t.replaceAll('ô', 'o')
            t = t.replaceAll('ú', 'u')
            t = t.replaceAll('Á', 'A')
            t = t.replaceAll('À', 'A')
            t = t.replaceAll('Â', 'A')
            t = t.replaceAll('Ã', 'A')
            t = t.replaceAll('É', 'E')
            t = t.replaceAll('Ê', 'E')
            t = t.replaceAll('Í', 'I')
            t = t.replaceAll('Ó', 'O')
            t = t.replaceAll('Õ', 'O')
            t = t.replaceAll('Ô', 'O')
            t = t.replaceAll('Ú', 'U')
            t = t.replaceAll('ª', 'a')
            t = t.replaceAll('º', 'o')
            t
        } else {
            s
        }
    }

    static String cpfToRaw(String cpf) {
        def raw = cpf
        if (raw) {
            raw = raw.replace('.', '')
            raw = raw.replace('-', '')
            sprintf("%011d", raw)
        } else {
            raw
        }
    }

    static String cnpjToRaw(String cnpj) {
        def raw = cnpj
        if (raw) {
            raw = raw.replace('.', '')
            raw = raw.replace('-', '')
            raw = raw.replace('/', '')
            sprintf("%014d", raw as long)
        } else {
            raw
        }

    }


    static String rawToCpf(String raw) {
        if (!raw) return null
        raw = raw.replace('.', '')
        raw = raw.replace('-', '')
        def a, b, c, d
        if (raw.size() >= 3)
            a = raw.substring(0, 3)
        else a = raw
        if (raw.size() >= 6)
            b = raw.substring(3, 6)
        else if (raw.size() > 3) b = raw.substring(3, raw.size())
        if (raw.size() >= 9)
            c = raw.substring(6, 9)
        else if (raw.size() > 6) c = raw.substring(6, raw.size())
        if (raw.size() >= 11)
            d = raw.substring(9, 11)
        else if (raw.size() > 9) d = raw.substring(9, raw.size())
        def r = ""
        if (a) r = a
        if (b) r = r + '.' + b
        if (c) r = r + '.' + c
        if (d) r = r + '-' + d
        r
    }

    static Date calcularDataProc() {
        def agora = new Date()
        def hora = agora[Calendar.HOUR_OF_DAY]
        def min = agora[Calendar.MINUTE]
        def dataRef = agora.clearTime()
        if (hora in (0..9) && min in (0..59)) dataRef--
        dataRef
    }

    static String normalize(str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD)
        str = str.replaceAll("[^\\p{ASCII}]", "")
        str
    }
}
