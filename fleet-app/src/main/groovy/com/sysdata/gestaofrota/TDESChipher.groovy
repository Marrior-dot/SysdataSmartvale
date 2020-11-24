package com.sysdata.gestaofrota

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class TDESChipher {

    private Cipher cipher
    private IvParameterSpec iv
    private SecretKey secretKey

    TDESChipher(String mode, String combinedKey) {

        this.cipher = Cipher.getInstance("DESede/" + mode + "/PKCS5Padding")

        if (mode == "CBC")
            this.iv = new IvParameterSpec(new byte[8])

        byte[] key = hexToBytes(combinedKey)

        // Se key com 16B, preenche para ficar com 24B
        byte[] keyBytes
        if (key.length == 16) {
            keyBytes = new byte[24]
            System.arraycopy(key, 0, keyBytes, 0, 16);
            System.arraycopy(key, 0, keyBytes, 16, 8);
        } else
            keyBytes = key

        this.secretKey = new SecretKeySpec(keyBytes, "DESede")

    }

    private int toDigit(char charHex) {
        int digit = Character.digit(charHex, 16)
        if (digit == -1)
            throw new IllegalArgumentException("Caracter Hexa inválido: $charHex")
        return digit
    }

    private byte hexToByte(String hexString) {
        int first = toDigit(hexString.charAt(0))
        int second = toDigit(hexString.charAt(1))
        return (byte) (first << 4) + second
    }


    private byte[] hexToBytes(String hexString) {
        if (hexString.length() % 2 != 0 )
            throw new IllegalArgumentException("String Hexadecimal inválida!")

        byte[] bytes = new byte[hexString.length() / 2]

        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2))
        }

        return bytes
    }


    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < bytes.length ; i++) {
            char[] hexDigits = new char[2]
            hexDigits[0] = Character.forDigit((bytes[i] >> 4) & 0xF, 16)
            hexDigits[1] = Character.forDigit((bytes[i] & 0xF), 16)
            sb.append(new String(hexDigits))
        }
        return sb.toString()
    }


    String encrypt(String plainText) {

        if (this.iv)
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, this.iv)
        else
            this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKey)

        return bytesToHex(this.cipher.doFinal(plainText.getBytes("utf-8")))

    }

    String decrypt(String hexCipherText) {

        byte[] textBytes = hexToBytes(hexCipherText)

        if (this.iv)
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey, this.iv)
        else
            this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey)

        return new String(this.cipher.doFinal(textBytes))

    }
}