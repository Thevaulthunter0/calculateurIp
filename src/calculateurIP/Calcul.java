package calculateurIP;

import java.math.BigInteger;

public class Calcul {

    public static String creerHexString(int start, int end, String hexDigit) {
        String hexStr = "";
        for (int i = start; i < end; i++) {
            hexStr += hexDigit;
        }
        return hexStr;
    }

    public static String obtientBinEnQuatreBits(String oldBinary) { //ex. binaire "11" à binaire "0011"
        String quatreBitBinaryStr = "";
        for (int i = 0; i < 4 - oldBinary.length(); i++) {
            quatreBitBinaryStr += "0";
        }
        quatreBitBinaryStr += oldBinary;
        return quatreBitBinaryStr;
    }

    public static String obtientBinEnNBits(String oldBinary, int subnetId) { //ex. pour /32, si oldBinary == "11" et subnetId == 32 => retourne binaire "0000 0011"
        String nBitBinaryStr = "";
        for (int i = 0; i < subnetId - oldBinary.length(); i++) {
            nBitBinaryStr += "0";
        }
        nBitBinaryStr += oldBinary;
        return nBitBinaryStr;
    }

    public static String manipulerQuatreBitBin(String oldQuatreBitStr, String bit, int prefix) {
        String newQuatreBitStr = "";
        int restant = prefix % 4; //1

        for (int i=0; i<restant; i++) {                                           //garder les bits à gauche
            newQuatreBitStr += oldQuatreBitStr.charAt(i);
        }
        for (int i=restant; i<4; i++) {                                           //mettre les bits à droite à '0'
            newQuatreBitStr += bit; //1 au lieu de 0 pour dernier adresse (comparé à la première adresse)
        }
        return newQuatreBitStr;
    }

//    public static String trouverHexDigit(int num) {
//        if (num % 4 == 0)  return "f";
//        if (num % 4 == 1)  return "7";
//        if (num % 4 == 2)  return "3";
//        if (num % 4 == 3)  return "1";
//        else return null;
//    }

    public static String convertirDecABin(int num) {
        String str = "";
        int nbDeDigitBin = (int)(Math.floor(Math.log(num)/Math.log(2)));
        for (int i=nbDeDigitBin; i>= 0; i--) {
            if (num - (int)Math.pow(2,i) >= 0) {
                str += "1";
                num -= (int)Math.pow(2,i);
            } else {
                str += "0";
            }
        }
        return str;
    }
    public static String convertirDecABin(BigInteger bi) {                  //obtient la forme binaire des BigInteger
        String str = "";
        int nbDeDigitBin = bi.bitLength();
        for (int i=nbDeDigitBin-1; i>= 0; i--) {
            BigInteger pow2 = BigInteger.ONE.shiftLeft(i);
            if (bi.compareTo(pow2)>=0) {
                str += "1";
                bi = bi.subtract(pow2);
            } else {
                str += "0";
            }
        }
        return str;
    }
    public static String convertirBinAHex(String bin) {
        String str = "";
        int accumulator = 0;

        for (int i = bin.length() - 1; i >= 0; i -= 4) {                //en groupes de 4 bits, le valeur du hexDigit est accumulé par accumulator
            int power = 0;
            for (int j = 0; j < 4; j++) {
                if (i - j >= 0 && bin.charAt(i - j) == '1') {
                    accumulator += Math.pow(2, power);
                }
                power++;
            }

            if (accumulator >= 10 && accumulator <= 15) {
                str = (char) ('a' + accumulator - 10) + str;                //convertir accumlateur en hexadécimal
            } else {
                str = accumulator + str;
            }

            accumulator = 0;                                                //reset accumulator pour prochain itération
        }
        return str;
    }

    public static int convertirHexDigitADec(char hexDigit) {                      //convertir un hexDigit en décimal
        switch (hexDigit) {
            case 'a': return 10;
            case 'b': return 11;
            case 'c': return 12;
            case 'd': return 13;
            case 'e': return 14;
            case 'f': return 15;
            default: return Character.digit(hexDigit, 16);             //retourner le caractère en entier
        }
    }


}
