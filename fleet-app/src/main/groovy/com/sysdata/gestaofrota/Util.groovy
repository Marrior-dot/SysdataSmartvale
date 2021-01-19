package com.sysdata.gestaofrota

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESedeKeySpec
import javax.crypto.spec.IvParameterSpec
import java.security.spec.KeySpec
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.Normalizer
import java.text.NumberFormat
import java.text.SimpleDateFormat

import com.sysdata.gestaofrota.exception.InvalidCurrencyException

class Util {

    static final String ACTION_NEW = "novo"
    static final String ACTION_VIEW = "visualizando"
    static final String ACTION_EDIT = "editando"
    static final String ACTION_FILTER = "filtrando"

    static final Locale LOCALE = new Locale("pt", "BR");

    static final int DIGITOS_SENHA  = 4
    static final int DIGITOS_CVV    = 3

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

    static BigDecimal convertToDouble(String value) {
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

    static def formatCurrency(val) {
        if (! val) val = 0
        NumberFormat nf = NumberFormat.getCurrencyInstance(LOCALE);
        nf.format(val)
    }

    static def parseCurrency(String val) {
        if (! val)
            val = "0,00"
        NumberFormat nf = val.contains("R\$") ? NumberFormat.getCurrencyInstance(LOCALE) : NumberFormat.getNumberInstance(LOCALE)
        return BigDecimal.valueOf(nf.parse(val))
    }


    static def formatPercentage(val) {
        if (! val) val = 0
        DecimalFormat df = new DecimalFormat("#0.00")
        df.format(val) + "%"
    }

    static def roundCurrency(curr) {
        if (curr instanceof Double) {
            BigDecimal convert = new BigDecimal(curr)
            double arrend = convert.setScale(2, BigDecimal.ROUND_HALF_UP);
            arrend
        }
    }

    static BigDecimal toBigDecimal(Double valor, int decimalPlace = 2) {
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

    static String normalize(str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD)
        str = str.replaceAll("[^\\p{ASCII}]", "")
        return str
    }

    static String stackTraceToString(t) {
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        t.printStackTrace(pw)
        return sw.toString()
    }


    static boolean validarCnpj( String strCnpj, formated = false) {
        def f= (!formated) || (strCnpj==~ /^[0-9]{2}\.[0-9]{3}\.[0-9]{3}\/[0-9]{4}-[0-9]{2}$/) || (strCnpj ==~ /^[0-9]{14}$/)
        if (f) {
            if (! strCnpj.substring(0,1).equals("")){
                try{
                    strCnpj=strCnpj.replace('.',' ');
                    strCnpj=strCnpj.replace('/',' ');
                    strCnpj=strCnpj.replace('-',' ');
                    strCnpj=strCnpj.replaceAll(" ","");
                    int soma = 0, aux, dig;
                    String cnpj_calc = strCnpj.substring(0,12);

                    if ( strCnpj.length() != 14 ) return false;
                    char[] chr_cnpj = strCnpj.toCharArray();
                    for( int i = 0; i < 4; i++ )
                        if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 ) soma += (chr_cnpj[i] - 48 ) * (6 - (i + 1)) ;
                    for( int i = 0; i < 8; i++ )
                        if ( chr_cnpj[i+4]-48 >=0 && chr_cnpj[i+4]-48 <=9 ) soma += (chr_cnpj[i+4] - 48 ) * (10 - (i + 1)) ;
                    dig = 11 - (soma % 11);
                    cnpj_calc += ( dig == 10 || dig == 11 ) ? "0" : Integer.toString(dig);
                    soma = 0;
                    for ( int i = 0; i < 5; i++ )
                        if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 ) soma += (chr_cnpj[i] - 48 ) * (7 - (i + 1)) ;
                    for ( int i = 0; i < 8; i++ )
                        if ( chr_cnpj[i+5]-48 >=0 && chr_cnpj[i+5]-48 <=9 ) soma += (chr_cnpj[i+5] - 48 ) * (10 - (i + 1)) ;
                    dig = 11 - (soma % 11);
                    cnpj_calc += ( dig == 10 || dig == 11 ) ? "0" : Integer.toString(dig);
                    return strCnpj.equals(cnpj_calc);
                }catch (Exception e){
                    return false;
                }
            }else return false;
        } else false

    }

    static boolean validarCpf(String strCpf,formated=false){
        def f= (!formated) || (strCpf==~ /^([0-9]{3}\.){2}[0-9]{3}-[0-9]{2}$/)
        if (f) {
            if (! strCpf.substring(0,1).equals("")){
                try{
                    boolean validado=true;
                    int     d1, d2;
                    int     dg1, dg2, resto;
                    int     dgCPF;
                    String  nDigResult;
                    strCpf=strCpf.replace('.',' ');
                    strCpf=strCpf.replace('-',' ');
                    strCpf=strCpf.replaceAll(" ","");
                    d1 = d2 = 0;
                    dg1 = dg2 = resto = 0;
                    for (int nCount = 1; nCount < strCpf.length() -1; nCount++) {
                        dgCPF = Integer.valueOf(strCpf.substring(nCount -1, nCount)).intValue();
                        d1 = d1 + ( 11 - nCount ) * dgCPF;
                        d2 = d2 + ( 12 - nCount ) * dgCPF;
                    };
                    resto = (d1 % 11);
                    if (resto < 2) dg1 = 0;
                    else dg1 = 11 - resto;
                    d2 += 2 * dg1;
                    resto = (d2 % 11);
                    if (resto < 2) dg2 = 0;
                    else dg2 = 11 - resto;
                    String nDigVerific = strCpf.substring(strCpf.length()-2, strCpf.length());
                    nDigResult = String.valueOf(dg1) + String.valueOf(dg2);
                    return nDigVerific.equals(nDigResult);
                }catch (Exception e){
                    return false;
                }
            }else return false;
        } else false
    }

    static def formatDomainErrors(object) {
        def errorMessages = []
        if (object.hasErrors()) {
            object.errors.allErrors.each {
                def fieldError = (it.defaultMessage) =~ /\{\d+\}/
                def error = it.defaultMessage
                if (fieldError) {
                    for (int i = 0; i < fieldError.size(); i++) {
                        error = error.replace(fieldError[i], it.arguments[i].toString())
                    }
                }
                errorMessages << error
            }
        }
        return errorMessages
    }

    static def calcularDataProcessamento() {
        def refDate = new Date()
        def hr = refDate[Calendar.HOUR_OF_DAY]
        def min = refDate[Calendar.MINUTE]
        if (hr in (0..18) && min in (0..59)) refDate--
        return refDate
    }

}
